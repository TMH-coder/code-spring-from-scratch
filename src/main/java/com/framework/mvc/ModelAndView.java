package com.framework.mvc;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ModelAndView {

    private String viewName;
    private Map<String,?> model;

    public ModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }
    public ModelAndView(String viewName){
        this.viewName = viewName;
    }


    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }

}
