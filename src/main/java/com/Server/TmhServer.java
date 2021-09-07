package com.Server;

import com.Server.Handle.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
/*
@作者：Tmh
@时间：6/9/2021 下午1:15
@功能描述:server类
*/
@Slf4j
public class TmhServer {
    private int port = 80;

    public void start(int port){
        EventLoopGroup boseEventLoop = new NioEventLoopGroup();
        EventLoopGroup workEventLoop = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boseEventLoop,workEventLoop)
                //.handler(new LoggingHandler(LogLevel.INFO))
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>(){

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        /*解析HTTP*/
                        p.addLast(new HttpServerCodec());
                        p.addLast(new HttpObjectAggregator(64 * 1024));
                        //.addLast(new ChunkedWriteHandler());//Inbound、Outbound
                        p.addLast(new HttpServerHandler());//Inbound
                    }
                });
        try {
            ChannelFuture f = serverBootstrap.bind(port).sync();
            System.out.println("服务器已启动在："+port+"端口");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boseEventLoop.shutdownGracefully();
            workEventLoop.shutdownGracefully();
        }
    }

    public void start(){
        start(this.port);
    }
    public static void main(String[] args) {
        if(args.length > 0){
            new TmhServer().start(Integer.valueOf(args[0]));
        }else{
            new TmhServer().start();
        }
    }
}

