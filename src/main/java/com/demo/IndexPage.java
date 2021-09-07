package com.demo;

import com.Server.Http.THttpResponse;
import com.Server.Http.THttpResquest;
import com.framework.annotation.TAutoWired;
import com.framework.annotation.TController;
import com.framework.annotation.TRequestMapping;
import com.framework.annotation.TRequestParam;
import com.framework.mvc.ModelAndView;
/**
*@author Tmh
*@date 7/9/2021 上午11:28
*@description 测试用得例子
**/
@TController("IndexPage")
@TRequestMapping("/")
public class IndexPage {
    @TAutoWired("Service")
    private Service service;

    @TRequestMapping("/index.html")
    public ModelAndView index(THttpResquest request, THttpResponse response, @TRequestParam("name") String name){
        service.f(name);
        return new ModelAndView("index.html");
    }
}
