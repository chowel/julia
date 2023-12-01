package com.julia.service.impl;

import com.google.gson.Gson;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.YaoMapper;
import com.julia.model.WebSocketMsgBO;
import com.julia.socket.ChannelPond;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-14 16:01
 **/
@Log4j2
@Service
public class WebSocketService {
    @Resource
    RedisUtils redisUtils;

    @Resource
    YaoMapper yaoMapper;

    private final Gson gson = new Gson();

    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = gson.fromJson(requestMsg, WebSocketMsgBO.class);
    }

    public void handleMsgWhitChannel(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = gson.fromJson(requestMsg, WebSocketMsgBO.class);
        if ("GETROCKET".equals(bo.getSub())) {
            handleConnect(channel);
        }
    }

    /**
     * @Description: ws 连接处理
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void handleConnect(Channel c) {
        String userId = ChannelPond.findUserIdByChannel(c);
        log.info("userId: " + userId);
        Channel userChannel = ChannelPond.findChannel(userId);
        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("userChannel: false");
        } else {
            WebSocketMsgBO bo = new WebSocketMsgBO();
            bo.setSub("CURARR");
            bo.setData(getRocketsByUserId(userId));
            userChannel.writeAndFlush(new TextWebSocketFrame(gson.toJson(bo)));
        }
    }

    /**
     * @Description: 收单分发
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void dispatcherRocket(String userId) {
        Channel userChannel = ChannelPond.findChannel(userId);

        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("userChannel: false");
        } else {
            WebSocketMsgBO bo = new WebSocketMsgBO();
            bo.setSub("DISARR");
            bo.setData(getRocketsByUserId(userId));
            userChannel.writeAndFlush(new TextWebSocketFrame(gson.toJson(bo)));
        }
    }

    public void joinZset(String userId) {
        YaoEntity yao = yaoMapper.selectById(Long.valueOf(userId));
        Boolean res = redisUtils.addZset(RedisKeyEnum.CAR_ALIVE.getKey(), userId, yao.getCoin());
        long exTime = redisUtils.getExpire(RedisKeyEnum.CAR_ALIVE.getKey());
        log.info("过期时间 :{}", exTime);
        // 设置缓存时间
        if (exTime == -1) {
            Long n = System.currentTimeMillis();
            Long t = JuliaUtils.todayTime();
            log.info("time {}-{}", n, t);
            int s = (int) ((t - n) / 1000);
            redisUtils.expire(RedisKeyEnum.CAR_ALIVE.getKey(), s);
        }
    }

    /**
     * @Description: 车队个人池计数
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void incrementScore(String userId, int count) {
        redisUtils.addScore(RedisKeyEnum.CAR_ALIVE.getKey(), userId, count);
    }

    public List<String> getAliveByZset(int minCoin) {
        Set<Object> res = redisUtils.rangeByScore(RedisKeyEnum.CAR_ALIVE.getKey(), minCoin, 10000000);
        return res.stream().map(e -> (String) e).collect(Collectors.toList());
    }

    /**
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void delByUserid(String userId) {
        redisUtils.removeByValue(RedisKeyEnum.CAR_ALIVE.getKey(), userId);
    }

    /**
     * @Description: 处理收银台过来的订单
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void handleDeposit(RocketEntity rocket) {

        List<String> userlist = getAliveByZset(rocket.getAmount());

        if (userlist.size() > 0) {
            // 放入公共池 过期时间10 分钟
            redisUtils.set(RedisKeyEnum.COMMON_POND.getKey() + userlist.get(0) + ":" + rocket.getOrderId(), rocket, 600);
            // 放入车队个人池
            redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + userlist.get(0), rocket.getOrderId(), rocket);

            long exTime = redisUtils.getExpire(RedisKeyEnum.CAR_POND.getKey() + userlist.get(0));

            if (exTime == -1) {
                Long n = System.currentTimeMillis();
                Long t = JuliaUtils.todayTime();
                int s = (int) ((t - n) / 1000);
                redisUtils.expire(RedisKeyEnum.CAR_POND.getKey() + userlist.get(0), s);
            }
            // 车队个人池计数+1
            int computeAmount = -rocket.getAmount();
            incrementScore(userlist.get(0), computeAmount);

            dispatcherRocket(userlist.get(0));
        } else {
            redisUtils.set(RedisKeyEnum.COMMON_POOL.getKey() + rocket.getOrderId(), rocket, 600);
        }
    }

    private List<RocketEntity> getRocketsByUserId(String userId){
        Map<Object, Object> map = redisUtils.hmget(RedisKeyEnum.CAR_POND.getKey() + userId);
        List<Object> list = new ArrayList<>(map.values());
        return list.stream().map(e -> (RocketEntity) e).collect(Collectors.toList());
    }
}
