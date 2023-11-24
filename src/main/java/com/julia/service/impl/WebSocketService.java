package com.julia.service.impl;

import com.google.gson.Gson;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.WebSocketMsgBO;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private final Gson gson = new Gson();

    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = gson.fromJson(requestMsg, WebSocketMsgBO.class);
    }

    public void handleMsgByChannel(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = gson.fromJson(requestMsg, WebSocketMsgBO.class);
//        if("START".equals(bo.getSub())){
//
//        }
    }

    public void joinZset(String userId) {
        Boolean res = redisUtils.addZset(RedisKeyEnum.CAR_ALIVE.getKey(), userId, 0);
        log.info("Zset添加是否: " + res);
    }

    public void incrementScore(String userId) {
        redisUtils.addScore(RedisKeyEnum.CAR_ALIVE.getKey(), userId, 1);
    }

    public List<String> getAliveByZset() {
        Set<Object> res = redisUtils.rangeByScore(RedisKeyEnum.CAR_ALIVE.getKey(), 0, 9999);
        return res.stream().map(e->(String)e).collect(Collectors.toList());
    }
    /**
    * @Description: 下线删除
    * @Param:
    * @return:
    * @Author: chowel
    * @Date:
    */
    public void delByUserid(String userId){
        redisUtils.removeByValue(RedisKeyEnum.CAR_ALIVE.getKey(),userId);
    }
}
