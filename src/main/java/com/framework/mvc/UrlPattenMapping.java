package com.framework.mvc;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.regex.Pattern;
/**
*@author Tmh
*@date 7/9/2021 下午2:38
*@description 保存url 模板和其所对应的method,这个名字不好，不知道取什么
**/
@Slf4j
public class UrlPattenMapping {
    private Pattern pattern;
    private Object instance;
    private Method method;

    public UrlPattenMapping(Pattern pattern, Object instance, Method method) {
        this.pattern = pattern;
        this.instance = instance;
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
