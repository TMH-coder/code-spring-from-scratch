package com.Server.Handle;

import com.Server.Http.THttpResponse;
import com.Server.Http.THttpResquest;
import com.framework.mvc.InitDispatchServerlet;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.net.URL;
/**
*@author Tmh
*@date 6/9/2021 下午5:40
*@description
*/
@Slf4j
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private URL baseUrl = HttpServerHandler.class.getClassLoader().getResource("");
    private final static String Properties = "tmh.properties";

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        InitDispatchServerlet serverlet = new InitDispatchServerlet();
        serverlet.init(Properties);

        THttpResquest request = new THttpResquest(channelHandlerContext,fullHttpRequest);
        THttpResponse response = new THttpResponse(channelHandlerContext, new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(),HttpResponseStatus.OK));

        if(fullHttpRequest.method() == HttpMethod.GET){
            serverlet.doGet(channelHandlerContext,request,response);
        }else if(fullHttpRequest.method() == HttpMethod.POST){
            serverlet.doPost(channelHandlerContext,request,response);
        }
        channelHandlerContext.close();


/*        FullHttpResponse response = new DefaultFullHttpResponse(fullHttpRequest.protocolVersion(),HttpResponseStatus.OK);
        response.content().writeBytes(Unpooled.wrappedBuffer("TMH".getBytes()));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        System.out.println(response.toString());*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Channel client = ctx.channel();
        System.out.println("Client:"+client.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
