package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.julia.entity.RocketEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.service.IRocketService;
import com.julia.tool.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-24 14:18
 **/
@Component
@Slf4j
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {
    @Resource
    RedisUtils redisUtils;

    @Resource
    IRocketService rocketService;

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString().replace("\"", "");
        log.info("监听到Key过期:{}", key);
        String[] keyArray = key.split(":");
        String orderId = "";
        if ("COMMON_POND".equals(keyArray[1])) {
            log.info("有车队公共池过期:{}", key);
            orderId = keyArray[3];
        }

        if ("COMMON_POOL".equals(keyArray[1])) {
            log.info("无车队公共池过期:{}", keyArray[2]);
            orderId = keyArray[2];
        }

        RocketEntity entity = rocketService.getOne(new QueryWrapper<RocketEntity>().eq("order_id", orderId));
        if (!ObjectUtils.isEmpty(entity)) {
            entity.setStatus(3);
            rocketService.updateById(entity);
        }
    }
}
