package com.framework.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanDefinition {
    private String beanName;
    private String className;

    public BeanDefinition(String beanName,String className){
        this.beanName = beanName;
        this.className = className;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
