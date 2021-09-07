package com.framework.bean;

import com.framework.annotation.TAutoWired;
import com.framework.annotation.TController;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApplicationContext {
    private BeanReader reader;
    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<String,BeanDefinition>();
    private Map<String,Object> factoryBeanObjectMap = new HashMap<>();
    private Map<String,BeanWrapper> factoryBeanWrapperMap = new HashMap<>();

    public ApplicationContext(String propertis) {
        try {
            reader = new BeanReader(propertis);
            List<BeanDefinition> beanDefinitions = reader.loadBeanDefinition();

            doRegistDefinition(beanDefinitions);
            doInitBean();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getFactorySize(){
        return factoryBeanWrapperMap.size();
    }

    public String[] getBeanNames(){
        return beanDefinitionMap.keySet().toArray(new String[beanDefinitionMap.size()]);
    }

    private void doInitBean() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

            Object instance = instationBean(beanName , beanDefinition);

            //factoryBeanWrapperMap这才是真正的IOC容器

            BeanWrapper beanWrapper = new BeanWrapper(instance,instance.getClass());
            factoryBeanWrapperMap.put(beanName,beanWrapper);

            //依赖注入
            populateBean(beanName,beanDefinition,beanWrapper);
        }
    }

    public Object getInstanceBybean(String beanName) {
        BeanWrapper beanWrapper = factoryBeanWrapperMap.get(beanName);
        return beanWrapper.getWrapperInstance();
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {
        Object instance = beanWrapper.getWrapperInstance();
        Class<?> clazz = beanWrapper.getWrapperClass();

//        if(! clazz.isAnnotationPresent(TController.class)){
//            return;
//        }

        for(Field field : clazz.getDeclaredFields()){
            if(! field.isAnnotationPresent(TAutoWired.class)){continue;}
            TAutoWired autoWired = field.getAnnotation(TAutoWired.class);
            String autoWiredName = autoWired.value().trim();
            //这里有点问题，因为beanDefinition中有一个beanName与className,如果不在@AutoWired中写上beanName的值，
            //此处获取的就是className，但是factoryBeanObjectMap存的是beanName,就会出现找不到的情况。
            if(autoWiredName.equals("")) {
                autoWiredName = field.getType().getName();
            }
            field.setAccessible(true);
            if(this.factoryBeanObjectMap.get(autoWiredName) == null){continue;}
            try {
                //执行依赖注入
                field.set(instance,this.factoryBeanObjectMap.get(autoWiredName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                continue;
            }

        }
    }

    private Object instationBean(String beanName , BeanDefinition beanDefinition) {
        String className = beanDefinition.getClassName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.getDeclaredConstructor().newInstance();
            this.factoryBeanObjectMap.put(beanName, instance);
            //this.factoryBeanObjectMap.put(className,instance);
        }catch (Exception e){
            e.printStackTrace();
        }
        return instance;
    }

    private void doRegistDefinition(List<BeanDefinition> beanDefinitions) throws Exception {
        for(BeanDefinition i : beanDefinitions ) {
            if(beanDefinitionMap.containsKey(i.getBeanName())){
                throw new Exception("类:"+i.getBeanName()+"已存在");
            }
          //  this.beanDefinitionMap.put(i.getClassName(), i);
            this.beanDefinitionMap.put(i.getBeanName(), i);
        }
    }

    public BeanReader getReader() {
        return reader;
    }
}
