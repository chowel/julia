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
import org.springframework.util.StringUtils;

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
        if ("CLIENTCONNECT".equals(bo.getSub())) {
            clientConnect(channel,(String)bo.getData());
        }
//        if ("GETFORTUNE".equals(bo.getSub())) {
//            fortuneToCar(channel);
//        }
        // 客户端心跳
        if ("CLIENTPING".equals(bo.getSub())) {

            String clientSecure = (String) bo.getData();
            if (StringUtils.hasLength(clientSecure)) {
                log.info("client-ping: 客户端 {} 心跳", clientSecure);
                Channel clientChannel = ChannelPond.findClientChannel(clientSecure);
                if (clientChannel != null) {
                    WebSocketMsgBO resBo = new WebSocketMsgBO();
                    resBo.setSub("CLIENTPONG");
                    resBo.setData(clientSecure);
                    clientChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(resBo)));
                }
            }

        }

//        // 心跳
//        if ("PING".equals(bo.getSub())) {
//            String userId = ChannelPond.findUserIdByChannel(channel);
//            if (StringUtils.hasLength(userId)) {
//                log.info("Heart->userId: " + userId);
//                handleHeart(userId);
//            } else {
//                channel.close();
//            }
//
//        }
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
        log.info("fortuneToCar-userId: " + userId);
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
    public void clientConnect(Channel c, String clientSecure) {

        Channel clientChannel = ChannelPond.findClientChannel(clientSecure);
        if(ObjectUtils.isEmpty(clientChannel)){
            ChannelPond.addClientChannel(c, clientSecure);
        }

        //  客户端连接返回
        WebSocketMsgBO clientbo = new WebSocketMsgBO();
        clientbo.setSub("CLIENTCONNECTED");
        clientbo.setData("OK");
        c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));
        // 通知后台 有客户端连接

    }

    @SneakyThrows
    public void handleHeart(String userId) {
//        String userId = ChannelPond.findUserIdByChannel(c);
//        log.info("HEART-userId: " + userId);
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
     * @Description: 收银台页面打开成功
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void depositsInput(String fortuneNo) {
        redisUtils.lSet(RedisKeyEnum.DEPOSIT.getKey(), fortuneNo, 24 * 3600);
    }


    /**
     * @Description: 收单分发
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void dispatcherFortune(String userId) {
        Channel userChannel = ChannelPond.findChannel(userId);
        fortuneToCar(userChannel);
    }

    public void joinZset(String userId) {
//        YaoEntity yao = yaoMapper.selectById(Long.valueOf(userId));
//        Boolean res = redisUtils.addZset(RedisKeyEnum.CAR_ALIVE.getKey(), userId, yao.getCoin());
//        long exTime = redisUtils.getExpire(RedisKeyEnum.CAR_ALIVE.getKey());
//        log.info("过期时间 :{}", exTime);
//        // 设置缓存时间
//        if (exTime == -1) {
//            Long n = System.currentTimeMillis();
//            Long t = JuliaUtils.todayTime();
//            log.info("time {}-{}", n, t);
//            int s = (int) ((t - n) / 1000);
//            redisUtils.expire(RedisKeyEnum.CAR_ALIVE.getKey(), s);
//        }
    }

    /**
     * @Description: 车队个人池计数
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void incrementScore(String userId, int count) {
//        redisUtils.addScore(RedisKeyEnum.CAR_ALIVE.getKey(), userId, count);
    }

    public List<String> getAliveByZset(int minCoin) {
//        Set<Object> res = redisUtils.rangeByScore(RedisKeyEnum.CAR_ALIVE.getKey(), minCoin, 10000000);
//        return res.stream().map(e -> (String) e).collect(Collectors.toList());
        return null;
    }

    /**
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void delByUserid(String userId) {

    }

    /**
     * @Description: 是否仅有自己在线
     * @Param:
     * @return: 只有自己在线或者没有人在线 返回 true
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
//            int rc = randomCarer(userlist.size());
            int rc = pollingCarId(userlist.size());
            log.info("Size：{}", userlist.size());
            log.info("rc：{}", rc);
            String carYaoId = userlist.get(rc);

            // 放入车队个人池 map
            redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + carYaoId, fortuneRedis.getFortuneNo(), fortuneRedis);
            // 放入财神池 过期时间10 分钟
            redisUtils.set(RedisKeyEnum.FORTUNE_POOL.getKey() + fortuneRedis.getFortuneNo() + ":" + carYaoId, fortuneRedis, 600);

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
        log.info("handOutRedis:CID:{}", entity.getCId());
        log.info("handOutRedis:FortuneNO:{}", entity.getFortuneNo());
        redisUtils.hdel(RedisKeyEnum.CAR_POND.getKey() + entity.getCId(), entity.getFortuneNo());
        redisUtils.del(RedisKeyEnum.FORTUNE_POOL.getKey() + entity.getFortuneNo() + ":" + entity.getCId());
    }

    /**
     * @Description: 重新分配
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public Boolean removeFortune(String fortuneNo, int carid) {

        FortuneRedis redisFortune =
                (FortuneRedis) redisUtils.get(RedisKeyEnum.FORTUNE_POOL.getKey() + fortuneNo + ":" + carid);
        if (!ObjectUtils.isEmpty(redisFortune)) {
            redisUtils.hdel(RedisKeyEnum.CAR_POND.getKey() + carid, fortuneNo);
            redisUtils.del(RedisKeyEnum.FORTUNE_POOL.getKey() + fortuneNo + ":" + carid);
            // todo
            List<String> userlist = getAliveByZset(redisFortune.getAmount());
            if (userlist.size() > 1) {
                String otherCarId = userlist.get(userlist.size() - 1);
                if (otherCarId.equals(String.valueOf(carid))) {
                    otherCarId = userlist.get(userlist.size() - 2);
                }// 放入车队个人池 map
                redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + otherCarId, redisFortune.getFortuneNo(), redisFortune);
                // 放入财神池 过期时间10 分钟
                redisUtils.set(RedisKeyEnum.FORTUNE_POOL.getKey() + redisFortune.getFortuneNo() + ":" + otherCarId, redisFortune, 600);
                dispatcherFortune(otherCarId);
                return true;
            }
        }
        return false;
    }

    public void removeByCarId(String carid) {
        List<FortuneRedis> fortuneList = getFortuneByCarId(carid);
        redisUtils.del(RedisKeyEnum.CAR_POND.getKey() + carid);
//        delByUserid(carid);
        if (fortuneList.size() > 0) {
            fortuneList.stream().forEach(f -> {
                redisUtils.del(RedisKeyEnum.FORTUNE_POOL.getKey() + f.getFortuneNo() + ":" + carid);
                List<String> ids = getAliveByZset(f.getAmount());
                if (ids.size() > 1) {
                    String otherCarId = ids.get(ids.size() - 1);
                    if (otherCarId.equals(carid)) {
                        otherCarId = ids.get(ids.size() - 2);
                    }
                    redisUtils.hset(RedisKeyEnum.CAR_POND.getKey() + otherCarId, f.getFortuneNo(), f);
                    // 放入财神池 过期时间10 分钟
                    redisUtils.set(RedisKeyEnum.FORTUNE_POOL.getKey() + f.getFortuneNo() + ":" + otherCarId,
                            f, 600);
                    dispatcherFortune(otherCarId);
                }
            });
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
