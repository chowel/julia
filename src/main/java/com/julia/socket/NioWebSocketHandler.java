package com.julia.socket;


import cn.dev33.satoken.stp.StpUtil;
import com.julia.service.impl.WebSocketService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-01-10 18:03
 **/
@Log4j2
@Component
public class NioWebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Resource
    private WebSocketService service;

    private WebSocketServerHandshaker handshaker;

    private static NioWebSocketHandler nioWebSocketHandler;


    @PostConstruct
    public void init() {
        nioWebSocketHandler = this;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            //以http请求形式接入，但是走的是websocket
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
//            log.info("处理websocket客户端的消息: " + msg);
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.info("客户端加入连接：" + ctx.channel());
//        ChannelSupervise.addChannel(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开连接
        String removeId = ChannelPond.removeChannel(ctx.channel());
        log.info("客户端断开连接：" + removeId);
        nioWebSocketHandler.service.delByUserid(removeId);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            log.info("本例程仅支持文本消息，不支持二进制消息");
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }
        // 收到消息体
        String request = ((TextWebSocketFrame) frame).text();
//        log.info(request);
        // 业务处理
//        nioWebSocketHandler.service.handleMsg(request);
        // 需要当前频道的业务处理
        nioWebSocketHandler.service.handleMsgWhitChannel(request, ctx.channel());

    }

    /**
     * 唯一的一次http请求，用于创建websocket
     */
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) {

        //要求Upgrade为websocket，过滤掉get/Post
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            //若不是websocket方式，则创建BAD_REQUEST的req，返回给客户端
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }


        log.info(req.uri());
        String[] uriArr = req.uri().split("/");
        if (uriArr.length > 2) {

            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    "websocket", null, false);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory
                        .sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }

            String id = (String) StpUtil.getLoginIdByToken(uriArr[2]);
            if (StringUtils.hasLength(id)) {
                ChannelPond.addChannel(ctx.channel(), id);
                // 加入
                nioWebSocketHandler.service.joinZset(id);
            }else{
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            }

        } else {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
    }

    /**
     * 拒绝不合法的请求，并返回错误信息
     */
    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, DefaultFullHttpResponse res) {
        // 返回应答给客户端
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        f.addListener(ChannelFutureListener.CLOSE);
        // 如果是非Keep-Alive，关闭连接
//        if (!isKeepAlive(req) || res.status().code() != 200) {
//            f.addListener(ChannelFutureListener.CLOSE);
//        }
    }
}
