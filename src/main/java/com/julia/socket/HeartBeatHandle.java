package com.julia.socket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-11-22 11:15
 **/
@Log4j2
@Component
public class HeartBeatHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        if(evt instanceof IdleState){
            IdleStateEvent idleStateEvent  = (IdleStateEvent) evt;

            if(idleStateEvent.state() == IdleState.READER_IDLE){
                log.info("读空闲...");
            }

            if(idleStateEvent.state() == IdleState.WRITER_IDLE){
                log.info("写空闲...");
            }

            if(idleStateEvent.state() == IdleState.ALL_IDLE){
                log.info("读写空闲 处理");

                Channel channel = ctx.channel();
                ChannelPond.removeChannel(ctx.channel());
                channel.close();
            }

        }
    }
}
