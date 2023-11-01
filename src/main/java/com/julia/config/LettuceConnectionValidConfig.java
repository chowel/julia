package com.julia.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2022-10-19 14:39
 **/
@Component
@Slf4j
public class LettuceConnectionValidConfig implements InitializingBean {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(redisConnectionFactory instanceof LettuceConnectionFactory){
            LettuceConnectionFactory c=(LettuceConnectionFactory)redisConnectionFactory;
            c.setValidateConnection(true);
        }
    }
}

