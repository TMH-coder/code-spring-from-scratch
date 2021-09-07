package com.framework.mvc;

import lombok.extern.slf4j.Slf4j;

import javax.management.DescriptorAccess;
import java.io.File;

@Slf4j
public class ViewReader {
    private String rootPath;
    private final String DefaultSuffix = ".html";

    public ViewReader(String tempateRoot) {
        rootPath = this.getClass().getClassLoader().getResource(tempateRoot).getPath();
        rootPath = rootPath.replaceAll("\\+","/");
    }
    View doRead(String viewName){
        if(viewName==null || "".equals(viewName)){
            return null;
        }
        viewName = viewName.endsWith(DefaultSuffix)? viewName : viewName+DefaultSuffix;
        File viewFile = new File((rootPath+"/"+viewName).replaceAll("/","/"));
        return new View(viewFile);
    }
}
