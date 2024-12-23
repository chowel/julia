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
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
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
                .localAddress(new InetSocketAddress(9098))
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        //设置log监听器，并且日志级别为debug，方便观察运行流程
//                        ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));
//                        //设置解码器
//                        ch.pipeline().addLast("http-codec",new HttpServerCodec());
//                        //聚合器，使用websocket会用到
//                        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
//                        //用于大数据的分区传输
//                        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
//                        //心跳配置
//                        ch.pipeline().addLast("Idle",new IdleStateHandler(2,4,60));
//                        //心跳处理
//                        ch.pipeline().addLast("HearBeat",new HeartBeatHandle());
//                        //自定义的业务handler
//                        ch.pipeline().addLast("handler",new NioWebSocketHandler());

                        ch.pipeline().addLast(new LoggingHandler(LogLevel.TRACE))
                                // HttpRequestDecoder和HttpResponseEncoder的一个组合，针对http协议进行编解码
                                .addLast(new HttpServerCodec())
                                // 分块向客户端写数据，防止发送大文件时导致内存溢出， channel.write(new ChunkedFile(new File("bigFile.mkv")))
                                .addLast(new ChunkedWriteHandler())
                                // 将HttpMessage和HttpContents聚合到一个完成的 FullHttpRequest或FullHttpResponse中,具体是FullHttpRequest对象还是FullHttpResponse对象取决于是请求还是响应
                                // 需要放到HttpServerCodec这个处理器后面
                                .addLast(new HttpObjectAggregator(10240))
                                // webSocket 数据压缩扩展，当添加这个的时候WebSocketServerProtocolHandler的第三个参数需要设置成true
                                .addLast(new WebSocketServerCompressionHandler())
                                // 聚合 websocket 的数据帧，因为客户端可能分段向服务器端发送数据
                                // https://github.com/netty/netty/issues/1112 https://github.com/netty/netty/pull/1207
                                .addLast(new WebSocketFrameAggregator(10 * 1024 * 1024))
                                // 服务器端向外暴露的 web socket 端点，当客户端传递比较大的对象时，maxFrameSize参数的值需要调大
                                .addLast(new WebSocketServerProtocolHandler("/websocket", null, true, 65536*10,false,
                                        true))
                                .addLast(new IdleStateHandler(2,4,60))
                                // 文本消息
                                .addLast(new TextWebSocketHandler())
                                // 二进制消息
                                .addLast(new BinaryWebSocketFrameHandler());

                    }
                });

        ChannelFuture channelFuturef = b.bind().sync();
        if (channelFuturef.isSuccess()){
            log.info("netty 启动成功 端口:9098");
        }


    }

}
