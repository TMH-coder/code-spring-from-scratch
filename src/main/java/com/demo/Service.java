package com.demo;

import com.framework.annotation.TController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TController("Service")
public class Service {
    public void f(String name){
    log.info("this is "+name);
    }

}
