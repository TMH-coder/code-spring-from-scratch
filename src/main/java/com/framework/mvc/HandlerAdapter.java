package com.framework.mvc;

import com.Server.Http.THttpResponse;
import com.Server.Http.THttpResquest;
import com.framework.annotation.TRequestParam;
import com.framework.util.Tutil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HandlerAdapter {

    //通过反射执行Method
    public ModelAndView handler(THttpResquest request, THttpResponse response,UrlPattenMapping mapping) throws Exception{

        Map<String,Integer> paramIndexMaping = new HashMap<>();

        //因为一个参数多个注解，所以是二维数组
        Annotation[][] paramsAnnotation = mapping.getMethod().getParameterAnnotations();
        for(int i = 0; i< paramsAnnotation.length; i++)
            for (Annotation p : paramsAnnotation[i]){
                if( p instanceof TRequestParam){
                    String paramName = ((TRequestParam) p).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMaping.put(paramName,i);
                    }
                }
            }

        Class<?>[] paramTypes = mapping.getMethod().getParameterTypes();
        for(int i=0; i< paramTypes.length;i++){
            if(paramTypes[i] == THttpResquest.class || paramTypes[i] == THttpResponse.class){
                paramIndexMaping.put(paramTypes[i].getName(),i);
            }
        }

        Object[] paramsValue = new Object[paramTypes.length];

        Map<String,String> paramFromClient = request.getParams();
        if(!paramFromClient.isEmpty()){
            paramFromClient.entrySet().forEach(entry->{
                if(paramIndexMaping.containsKey(entry.getKey())){
                    int i = paramIndexMaping.get(entry.getKey());
                    paramsValue[i] = Tutil.castStringValue(entry.getValue(),paramTypes[i]);
                }
            });
        }
        if(paramIndexMaping.containsKey(THttpResquest.class.getName())){
            int i = paramIndexMaping.get(THttpResquest.class.getName());
            paramsValue[i] = request;
        }
        if(paramIndexMaping.containsKey(THttpResponse.class.getName())){
            int i = paramIndexMaping.get(THttpResponse.class.getName());
            paramsValue[i] = response;
        }

        Object result = mapping.getMethod().invoke(mapping.getInstance(),paramsValue);
        if(result == null || result instanceof Void){return null;}

        boolean isModelAndView = mapping.getMethod().getReturnType() == ModelAndView.class;
        if(isModelAndView){
            return (ModelAndView)result;
        }

        return  null;

    }
}
