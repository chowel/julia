package com.julia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.julia.entity.FortuneEntity;
import com.julia.entity.RocketEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.service.IFortuneService;
import com.julia.service.IRocketService;
import com.julia.tool.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

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
    IFortuneService fortuneService;

    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString().replace("\"", "");
        log.info("监听到Key过期:{}", key);
        String[] keyArray = key.split(":");
        String cId = "";

        if ("FORTUNE_POOL".equals(keyArray[1])) {
            log.info("FORTUNE-收单过期-key:{}", key);
            String orderId = keyArray[2];
            FortuneEntity fortune = fortuneService.getOne(new QueryWrapper<FortuneEntity>().eq("order_id", orderId));
            if (!ObjectUtils.isEmpty(fortune)) {
                fortune.setStatus(3);
                fortuneService.updateById(fortune);
            }
        }

    }
}
