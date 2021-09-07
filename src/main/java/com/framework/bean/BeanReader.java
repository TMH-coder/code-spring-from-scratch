package com.framework.bean;

import com.framework.annotation.TController;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.framework.bean.PropertiesItem.ScanPackage;

@Slf4j
public class BeanReader {
    private List<String> readeClassNames = new ArrayList<String>();
    private Properties config = new Properties();

    public BeanReader(String properties){
         doloadConfig(properties);
         doScan(config.getProperty(ScanPackage));
    }

    public List<String> getReadeClassNames() {
        return readeClassNames;
    }

    public Properties getConfig(){
        return  config;
    }

    private void doScan(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource(scanPackage.replaceAll("\\.","/"));
        if(url.getFile() == null){
            log.info(url.toString()+"目录下没有类");
            return;
        }
        File classes = new File(url.getFile());
        for(File file : classes.listFiles()){
            if(file.isDirectory()){
                doScan(scanPackage+"."+file.getName());//因为要scan的文件加下还可能有文件夹，递归
            }else{
                if(!file.getName().endsWith(".class")){continue;}
                //此处应为全类名，因为class.forName要用全类名
                String className = scanPackage + "." + file.getName().replace(".class", "");
                readeClassNames.add(className);
            }
        }

    }

    private void doloadConfig(String properties) {
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(properties);
        try {
            config.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BeanDefinition> loadBeanDefinition(){
        List<BeanDefinition> results = new ArrayList<BeanDefinition>();
        for(String name : readeClassNames){
            try {
                Class<?> clazz = Class.forName(name);
                if(clazz.isInterface()) {continue;}
                if(! clazz.isAnnotationPresent(TController.class)){continue;}

                TController  controller = clazz.getAnnotation(TController.class);
                String beanName = controller.value().trim();
                //这里要是全类名
                String className = clazz.getName();
                if("".equals(beanName)){beanName = className;}
                results.add(new BeanDefinition(beanName,className));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return results;
    }
}
