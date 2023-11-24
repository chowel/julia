package com.julia.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

/**
 * @program: skychain
 * @description:
 * @author: Chowel.Master
 * @create: 2023-01-10 17:15
 **/
@Component
@Log4j2
public class NettyServer {
    @PostConstruct
    public void start() throws Exception {

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(9099))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //设置log监听器，并且日志级别为debug，方便观察运行流程
                        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));
                        //设置解码器
                        ch.pipeline().addLast("http-codec",new HttpServerCodec());
                        //聚合器，使用websocket会用到
                        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                        //用于大数据的分区传输
                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        //心跳配置
                        ch.pipeline().addLast("Idle",new IdleStateHandler(2,4,60));
                        //心跳处理
                        ch.pipeline().addLast("HearBeat",new HeartBeatHandle());
                        //自定义的业务handler
                        ch.pipeline().addLast("handler",new NioWebSocketHandler());

                    }
                });

        ChannelFuture channelFuturef = b.bind().sync();
        if (channelFuturef.isSuccess()){
            log.info("netty 启动成功 端口:8804");
        }


    }

}
