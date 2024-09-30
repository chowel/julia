package com.julia.service.impl;

import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-09-30 10:30
 **/
@Log4j2
@Service
public class PokerWsService {

    @Resource
    RedisUtils redisUtils;

    @SneakyThrows
    public void handleVhannelMsg(String requestMsg, Channel channel) {

    }
}
