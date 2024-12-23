package com.julia.socket;

import com.julia.service.impl.PokerWsService;
import com.julia.tool.PlayerToken;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-11-18 17:19
 **/
@Slf4j
@Component
public class TextWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Resource
    private PokerWsService service;

    private static TextWebSocketHandler textWebSocketHandler;


    @PostConstruct
    public void init() {
        textWebSocketHandler = this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
//        log.info("接收到客户端的消息:[{}]", msg.text());
        textWebSocketHandler.service.handleChannelMsg(msg.text(), ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        String removeId = ChannelPond.removeChannel(ctx.channel());
        log.info("客户端断开连接：" + removeId);
        textWebSocketHandler.service.delByUserId(removeId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.error("服务器发生了异常:", cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("web socket 握手成功。");
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String requestUri = handshakeComplete.requestUri();
            String[] uriArr = requestUri.split("/");
            if (uriArr.length > 2) {
                String id = (String) PlayerToken.getLoginIdByToken(uriArr[2]);
                if (StringUtils.hasLength(id)) {
                    log.info("玩家Id:[{}] 加入", id);
                    ChannelPond.addChannel(ctx.channel(), id);
                }
            }
            String subproTocol = handshakeComplete.selectedSubprotocol();
            log.info("subproTocol:[{}]", subproTocol);
            handshakeComplete.requestHeaders().forEach(entry -> log.info("header key:[{}] value:[{}]", entry.getKey(), entry.getValue()));
        } else if (evt instanceof IdleState) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.info("读空闲...");
            }

            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("写空闲...");
            }

            if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("读写空闲 处理");

                Channel channel = ctx.channel();
                ChannelPond.removeChannel(ctx.channel());
                channel.close();
            }

        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
