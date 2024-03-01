package com.julia.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.FortuneEntity;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.YaoMapper;
import com.julia.model.WebSocketMsgBO;
import com.julia.model.WsFortunneBO;
import com.julia.model.dto.FortuneRedis;
import com.julia.socket.ChannelPond;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.security.SecureRandom;
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


    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
    }

    @SneakyThrows
    public void handleMsgWhitChannel(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
        if ("GETROCKET".equals(bo.getSub())) {
            handleConnect(channel);
        }
        if ("GETFORTUNE".equals(bo.getSub())) {
            fortuneToCar(channel);
        }
        // 心跳
        if ("PING".equals(bo.getSub())) {
            handleHeart(channel);
        }
    }

    /**
     * @Description: 分发fortune
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void fortuneToCar(Channel c) {

        String userId = ChannelPond.findUserIdByChannel(c);
        log.info("userId: " + userId);
        Channel userChannel = ChannelPond.findChannel(userId);
        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("userChannel: false");
        } else {
            WsFortunneBO bo = new WsFortunneBO();
            bo.setSub("FORLIST");
            bo.setList(getFortuneByCarId(userId));
            userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));

        }

    }

    /**
     * @Description: ws 连接处理
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
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
            userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));

        }
    }

    @SneakyThrows
    public void handleHeart(Channel c) {
        String userId = ChannelPond.findUserIdByChannel(c);
        log.info("HEART-userId: " + userId);
        Channel userChannel = ChannelPond.findChannel(userId);
        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("HEART-Channel: None");
        } else {
            WebSocketMsgBO bo = new WebSocketMsgBO();
            bo.setSub("PONG");
            bo.setData("");
            userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
        }
    }

    /**
     * @Description: 收单分发
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void dispatcherRocket(String userId) {
        Channel userChannel = ChannelPond.findChannel(userId);

        if (ObjectUtils.isEmpty(userChannel)) {
            log.info("userChannel: false");
        } else {
            WebSocketMsgBO bo = new WebSocketMsgBO();
            bo.setSub("DISARR");
            bo.setData(getRocketsByUserId(userId));
            userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
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
     * @Description: 是否仅有自己在线
     * @Param:
     * @return: 只有自己在线或者没有人在线 true
     * @Author: chowel
     * @Date:
     */
    public boolean checkAlive(int carId, int minCoin) {
        List<String> ids = getAliveByZset(minCoin);
        if (ids.size() > 1) {
            return false;
        }
        if (ids.size() == 1) {
            String aliveId = ids.get(0);
            if (aliveId.equals(String.valueOf(carId))) {
                return true;
            } else {
                return false;
            }
        }
        return true;
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

    /**
     * @Description: 处理财神单
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public Boolean hanldeFortune(FortuneRedis fortuneRedis) {
        List<String> userlist = getAliveByZset(fortuneRedis.getAmount());
        if (userlist.size() > 0) {
//
            // 放入财神池 过期时间10 分钟
            redisUtils.set(RedisKeyEnum.FORTUNE_POOL.getKey() + fortuneRedis.getFortuneNo(), fortuneRedis, 600);
//            int rc = randomCarer(userlist.size());
            int rc = pollingCarId(userlist.size());
            log.info("Size：{}", userlist.size());
            log.info("rc：{}", rc);
            String carYaoId = userlist.get(rc);

            // 放入车队个人池 map
            redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + carYaoId, fortuneRedis.getFortuneNo(), fortuneRedis);

            Channel userChannel = ChannelPond.findChannel(carYaoId);

            if (ObjectUtils.isEmpty(userChannel)) {
                log.info("no car on line");
                return false;
            } else {
                WsFortunneBO bo = new WsFortunneBO();
                bo.setSub("FORLIST");
                bo.setList(getFortuneByCarId(carYaoId));
                userChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
                return true;
            }
        }
        return false;
    }

    /**
     * @Description:
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void handOutRedis(FortuneEntity entity) {
        redisUtils.hdel(RedisKeyEnum.CAR_POND.getKey() + entity.getCId(), entity.getFortuneNo());
        redisUtils.del(RedisKeyEnum.FORTUNE_POOL.getKey() + entity.getFortuneNo());
    }

    public void removeFortune(String fortuneNo, int carid) {

        FortuneRedis redisFortune = (FortuneRedis) redisUtils.get(RedisKeyEnum.FORTUNE_POOL.getKey() + fortuneNo);
        if (!ObjectUtils.isEmpty(redisFortune)) {
            redisUtils.hdel(RedisKeyEnum.CAR_POND.getKey() + carid, fortuneNo);
            hanldeFortune(redisFortune);
        }
    }

    public void removeByCarId(String carid) {
        List<FortuneRedis> list = getFortuneByCarId(carid);
        redisUtils.del(RedisKeyEnum.CAR_POND.getKey() + carid);
        delByUserid(carid);
        if (list.size() > 0) {
            List<String> ids = getAliveByZset(0);
            for (String id : ids) {
                if (!id.equals(carid)) {
                    list.stream().forEach(e->{
                        redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + id, e.getFortuneNo(), e);
                    });
                }
            }
        }
    }

    /**
     * @Description: 处理车队操作
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void handleCarOpera(YaoEntity yao, RocketEntity rocket) {
        // 车队个人池中删除
        redisUtils.hdel(RedisKeyEnum.CAR_POND.getKey() + yao.getYaoId(), rocket.getOrderId());
        // 公共池中删除
        redisUtils.del(RedisKeyEnum.COMMON_POND.getKey() + yao.getYaoId() + rocket.getOrderId());

        String yaoId = String.valueOf(yao.getYaoId());

        redisUtils.addZset(RedisKeyEnum.CAR_ALIVE.getKey(), yaoId, yao.getCoin());

        Channel userChannel = ChannelPond.findChannel(String.valueOf(yao.getYaoId()));
        handleConnect(userChannel);
    }

    private List<RocketEntity> getRocketsByUserId(String userId) {
        Map<Object, Object> map = redisUtils.hmget(RedisKeyEnum.CAR_POND.getKey() + userId);
        List<Object> list = new ArrayList<>(map.values());
        return list.stream().map(e -> (RocketEntity) e).collect(Collectors.toList());
    }

    /**
     * @Description: 获取当前财神
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public List<FortuneRedis> getFortuneByCarId(String userId) {
        Map<Object, Object> map = redisUtils.hmget(RedisKeyEnum.CAR_POND.getKey() + userId);
        List<Object> list = new ArrayList<>(map.values());
        return list.stream().map(e -> (FortuneRedis) e).collect(Collectors.toList());
    }

    private int randomCarer(int max) {
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextInt(max);
    }

    /**
     * @Description: 轮询
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public int pollingCarId(int size) {
        if (redisUtils.hasKey(RedisKeyEnum.POLLING.getKey())) {
            int curPolling = (int) redisUtils.get(RedisKeyEnum.POLLING.getKey());
            if (curPolling >= size) {
                redisUtils.decr(RedisKeyEnum.POLLING.getKey(), curPolling);
                return 0;
            } else {
                redisUtils.incr(RedisKeyEnum.POLLING.getKey(), 1);
                return (int) curPolling;
            }
        } else {
            redisUtils.set(RedisKeyEnum.POLLING.getKey(), 0);
            return 0;
        }


    }
}
