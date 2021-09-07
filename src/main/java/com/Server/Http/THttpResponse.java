package com.Server.Http;

import com.framework.util.Tutil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetEncoder;

@Slf4j
public class THttpResponse {
    private ChannelHandlerContext ctx;
    private FullHttpResponse response;
    private HttpHeaders headers;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public FullHttpResponse getResponse() {
        return response;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }


    public THttpResponse(ChannelHandlerContext ctx,FullHttpResponse response) {
        this.ctx = ctx;
        this.response = response;
        this.headers = response.headers();
    }

    public void write(File file) {
        try {
            if(file == null || file.length() ==0) {
                log.debug("the file is invalid");
                return;
            }

            RandomAccessFile accessFile = new RandomAccessFile(file,"r");
            FileChannel readChannel = accessFile.getChannel();
            MappedByteBuffer buffer = readChannel.map(FileChannel.MapMode.READ_ONLY,0,readChannel.size());
            ByteBuf buf = Unpooled.wrappedBuffer(buffer);
            response.content().writeBytes(buf);
            log.debug("the response's content is \r\n"+Tutil.converByteBufToString(response.content()));
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }

    public void setContentType(String contentType) {
        headers.set(HttpHeaderNames.CONTENT_TYPE, contentType + " charset=utf-8;");
    }
    public void setContenLength(long length){
        String headContengLength = headers.getAsString(HttpHeaderNames.CONTENT_LENGTH);
        headers.set(headContengLength, length);
    }

}
