package com.framework.util;

import io.netty.buffer.ByteBuf;
/**
*@author ：Tmh
*@date: 6/9/2021 下午5:09
*@description :工具类
*{@link #converByteBufToString(ByteBuf)} :将ByteBuf转为String
*
**/
public class Tutil {
    public  static String converByteBufToString(ByteBuf buf){
        String str;
        if(buf.hasArray()){
            str = new String(buf.array(),buf.arrayOffset()+buf.readerIndex(),buf.readableBytes());
        }else{
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(),bytes);
            str = new String(bytes,0,buf.readableBytes());
        }
        return str;
    }
    public static Object castStringValue(String value, Class<?> paramType) {
        if(String.class == paramType){
            return value;
        }else if(Integer.class == paramType){
            return Integer.valueOf(value);
        }else if(Double.class == paramType){
            return Double.valueOf(value);
        }else {
            if(value != null){
                return value;
            }
            return null;
        }

    }
}
