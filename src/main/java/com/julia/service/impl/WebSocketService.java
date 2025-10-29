package com.julia.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.julia.enums.RedisKeyEnum;
import com.julia.model.*;
import com.julia.model.game.AppearRole;
import com.julia.model.game.MinaGame;
import com.julia.service.IMinaGameService;
import com.julia.socket.ChannelPond;
import com.julia.tool.GameUtils;
import com.julia.tool.RedisUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
    IMinaGameService minaGameService;


    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void handleMsg(String requestMsg) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
    }

    @SneakyThrows
    public void handleBinaryMsg(ByteBuffer buffer, Channel channel) {

        // 消息类型 66 心跳
        short msgType = buffer.getShort();

        // 处理心跳 服务端 66 客户端 67
        if (msgType == 66) {
            ByteBuf byteBuf = channel.alloc().buffer(2);
            byteBuf.writeShort(67);
            channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
        }

        // 游戏84 发放游戏
        if (msgType == 84) {
            String playerId = ChannelPond.findClientIdByChannel(channel);
            if (StringUtils.hasLength(playerId)) {
                List<MinaGame> games = minaGameService.genGameByPlayerId(Long.valueOf(playerId),8,1);
                ByteBuf byteBuf = Unpooled.buffer();
                byteBuf.writeShort(85);
                String jsonGame = mapper.writeValueAsString(games);
                byteBuf.writeBytes(jsonGame.getBytes(StandardCharsets.UTF_8));
                log.info("Len: {}", byteBuf.readableBytes());
                channel.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
            }
        }

        // 游戏84 上报游戏结果
        if (msgType == 86) {
            // 获取剩余可读字节
            int remaining = buffer.remaining();
            // 提取剩余字节
            byte[] bytes = new byte[remaining];
            // 将剩余字节读入数组，position 自动移动到末尾
            buffer.get(bytes);
            // 转换为字符串（推荐 UTF-8）
            String text = new String(bytes, StandardCharsets.UTF_8);
            log.info("剩余字符串: " + text);

            AppearRole appearRole = mapper.readValue(text, AppearRole.class);

            log.info("GameNo: {}",appearRole.getGameNo());
        }
    }

    @SneakyThrows
    public void handleMsgWhitChannel(String requestMsg, Channel channel) {
        WebSocketMsgBO bo = mapper.readValue(requestMsg, WebSocketMsgBO.class);
        // 客户端连接
        if ("CLIENTCONNECT".equals(bo.getSub())) {
            clientConnect(channel, bo);
        }
        if ("ADMINCONNECT".equals(bo.getSub())) {
            adminConnect(channel);
        }
        // 后台发往客户端
        if ("TOCLIENT".equals(bo.getSub())) {
            adminToClient(bo);
        }
        // 客户端页面提交
        if ("CLIENTPAGENEXT".equals(bo.getSub())) {
            clentPageNext(channel, bo);
        }

        // 客户端当前页面
        if ("CURRENTPAGE".equals(bo.getSub())) {
            clentPage(bo);
        }
        // 客户端心跳
        if ("client_ping".equals(bo.getSub())) {

            String clientSecure = (String) bo.getData();
            if (StringUtils.hasLength(clientSecure)) {
                log.info("client-ping: 客户端 {} 心跳", clientSecure);

//                Channel clientChannel = ChannelPond.findClientChannel(clientSecure);
//                if (clientChannel != null) {
                WebSocketMsgBO resBo = new WebSocketMsgBO();
                resBo.setSub("client_pong");
                resBo.setData("client_pong");
                channel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(resBo)));
//                }
            }
        }


    }

    /**
     * @Description: 后台发往前台
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    private void adminToClient(WebSocketMsgBO bo) {
        log.info("后台提交前端: {}", bo.getSub());
        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;
            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                //                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            } else {
                clientChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(bo)));
            }
        }
    }

    @SneakyThrows
    private void clentPageNext(Channel c, WebSocketMsgBO bo) {
        log.info("客户端提交当前页面: {}", bo.getSub());
        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            }
//            msgMap.put("res","OK");
//            //  客户端连接返回 测试 实际没有返回
//            WebSocketMsgBO clientbo = new WebSocketMsgBO();
//            clientbo.setSub(bo.getSub());
//            clientbo.setData(msgMap);
//            c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));

            // 通知后台 有客户端连接
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTPAGENEXT");
            adminInform.setData(bo.getData());
            adminSendMsg(adminInform);
        }
    }

    /**
     * @Description: 客户端当前页面
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    private void clentPage(WebSocketMsgBO bo) {
        log.info("客户端当前页面: {}", bo.getSub());
        // 通知后台 有客户端当前页面
        adminSendMsg(bo);
    }

    /**
     * @Description: 后台连接
     * @Param:
     * @return:
     * @Author: chowel
     * @Date: ADMINERROR 0：连接失败
     */
    @SneakyThrows
    public void adminConnect(Channel c) {

        String adminId = ChannelPond.findAdminByChannel(c);
        WebSocketMsgBO adminMsg = new WebSocketMsgBO();
        if (StringUtils.hasLength(adminId)) {
            adminMsg.setSub("ADMINCONNECTED");
            adminMsg.setData(1);
        } else {
            adminMsg.setSub("ADMINERROR");
            adminMsg.setData(0);
        }

        c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminMsg)));


    }


    /**
     * @Description: ws 客户端连接
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    @SneakyThrows
    public void clientConnect(Channel c, WebSocketMsgBO bo) {

        Object msgData = bo.getData();
        if (msgData instanceof Map) {
            Map<String, Object> msgMap = (Map<String, Object>) msgData;

            Channel clientChannel = ChannelPond.findClientChannel((String) msgMap.get("secure"));
            if (ObjectUtils.isEmpty(clientChannel)) {
                ChannelPond.addClientChannel(c, (String) msgMap.get("secure"));
            }
//            yaoClientService.saveFromConnect((String) msgMap.get("secure"), (String) msgMap.get("secure"));
            //  客户端连接返回
//            WebSocketMsgBO clientbo = new WebSocketMsgBO();
//            clientbo.setSub("CLIENTCONNECTED");
//            clientbo.setData("OK");
//            c.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(clientbo)));

            // 通知后台 有客户端连接
//            WebSocketMsgBO adminInform = new WebSocketMsgBO();
//            adminInform.setSub("CLIENTIN");
//            adminInform.setData(bo.getData());
//            adminSendMsg(adminInform);

            byte[] responseData = "Hell".getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.copiedBuffer(responseData);
            c.writeAndFlush(new BinaryWebSocketFrame(buf));
        }


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
     * @Description: 下线删除
     * @Param:
     * @return:
     * @Author: chowel
     * @Date:
     */
    public void disconnect(Channel c) {
        String res = ChannelPond.removeChannel(c);
        if (res.length() > 9) {
            log.info("客户端退出");
            WebSocketMsgBO adminInform = new WebSocketMsgBO();
            adminInform.setSub("CLIENTOUT");
            adminInform.setData(res);
            adminSendMsg(adminInform);
        }
    }

    @SneakyThrows
    protected void adminSendMsg(WebSocketMsgBO adminInform) {
        ConcurrentHashMap<String, ChannelId> admins = ChannelPond.getAllAdmin();
        for (ChannelId channelId : admins.values()) {
            Channel adminChannel = ChannelPond.findAdminChannelByChannelId(channelId);
            if (!ObjectUtils.isEmpty(adminChannel)) {
                adminChannel.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(adminInform)));
            }
        }
    }

}
