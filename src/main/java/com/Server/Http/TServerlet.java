package com.Server.Http;

import io.netty.channel.ChannelHandlerContext;

public interface TServerlet {

    public void doPost(ChannelHandlerContext ctx,THttpResquest resquest,THttpResponse response);

    public void doGet(ChannelHandlerContext ctx,THttpResquest resquest,THttpResponse response);
}
