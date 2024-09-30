package com.julia.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.PlayerRo;
import com.julia.model.WebSocketMsgBO;
import com.julia.socket.ChannelPond;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Set;

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
            String key = RedisKeyEnum.ROOMPLAYERS.getKey()+(String)bo.getData();
            Set<Object> players = redisUtils.sGet(key);
            for (Object element : players) {
                PlayerRo t = (PlayerRo) element;

                if(!t.getPlayId().equals(Long.valueOf(userId))){
                    log.info(t.getNickName());
                    Channel userChannel= ChannelPond.findChannel(String.valueOf(t.getPlayId()));
                    if(!ObjectUtils.isEmpty(userChannel)){
//                        userChannel.writeAndFlush();
                    }
                }
            }
        }
    }
}
