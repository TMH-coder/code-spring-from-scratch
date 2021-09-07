package com.framework.bean;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
//这个类存在实际上是为了aop而存在的
@Slf4j
public class BeanWrapper {
    private Object wrapperInstance;
    private Class<?> wrapperClass;

    public BeanWrapper(Object instance, Class<?> instanceClass){
        wrapperInstance = instance;
        wrapperClass = instanceClass;
    }

    public Class<?> getWrapperClass() {
        return wrapperClass;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }
}
