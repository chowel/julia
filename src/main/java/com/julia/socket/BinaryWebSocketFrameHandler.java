package com.julia.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program: julia
 * @description:
 * @author: Chowel.Master
 * @create: 2024-11-18 16:27
 **/
@Slf4j
@Component
public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame binaryWebSocketFrame) throws Exception {
        log.info("服务器接收到二进制消息,消息长度:[{}]", binaryWebSocketFrame.content().capacity());
        ByteBuf byteBuf = Unpooled.directBuffer(binaryWebSocketFrame.content().capacity());
        byteBuf.writeBytes(binaryWebSocketFrame.content());
        ctx.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
    }

}
