package com.Server.Http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class THttpResquest {
    private ChannelHandlerContext ctx; //SocketChannel的封装
    private FullHttpRequest httpRequest;

    public THttpResquest(ChannelHandlerContext ctx,FullHttpRequest request) {
        this.ctx = ctx;
        this.httpRequest = request;
    }

    public String getUrl() {
        return httpRequest.uri();
    }

    public String getMethod() {
        return httpRequest.method().name();
    }

    public Map<String, String> getParams(){
        Map<String,String> paramsMapping = new HashMap<>();
        if(HttpMethod.GET == httpRequest.method()){
            QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
            decoder.parameters().entrySet().forEach(entry->{
                paramsMapping.put(entry.getKey(),entry.getValue().get(0));
            });
        }else if(HttpMethod.POST == httpRequest.method()){
            HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(httpRequest);
            postDecoder.offer(httpRequest);
            List<InterfaceHttpData> list = postDecoder.getBodyHttpDatas();
            for(InterfaceHttpData i : list){
                Attribute a = (Attribute)  i;
                try {
                    paramsMapping.put(a.getName(),a.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return paramsMapping;
    }

}
