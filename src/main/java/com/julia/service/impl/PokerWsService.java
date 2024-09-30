package com.julia.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.model.WebSocketMsgBO;
import com.julia.socket.ChannelPond;
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

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleChannelMsg(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);

        if("JOINROOM".equals(bo.getSub())){
            String userId = ChannelPond.findUserIdByChannel(channel);
            log.info("加入房间->userId: " + userId);
            log.info((String)bo.getData());
        }
    }
}
