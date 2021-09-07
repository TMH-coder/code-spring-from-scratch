package com.framework.mvc;

import com.Server.Http.THttpResponse;
import com.Server.Http.THttpResquest;
import com.Server.Http.TServerlet;
import com.framework.annotation.TRequestMapping;
import com.framework.bean.ApplicationContext;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.framework.bean.PropertiesItem.TemplateRoot;

@Slf4j
public class InitDispatchServerlet implements TServerlet {
    private ApplicationContext applicationContext;
    private List<UrlPattenMapping> urlPattenList = new ArrayList<UrlPattenMapping>();
    private ViewReader viewReader;
    private Map<UrlPattenMapping,HandlerAdapter> handlerAdapterMap = new HashMap<>();

    public void init(String propertis){
        try {
            applicationContext = new ApplicationContext(propertis);
            initStrategies(applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initStrategies(ApplicationContext applicationContext) {
        initHandleMapings(applicationContext);

        initHandleAdapter(applicationContext);

        initViewReader(applicationContext);
    }

    private void initHandleAdapter(ApplicationContext applicationContext) {
        for (UrlPattenMapping handermapping : urlPattenList){
            handlerAdapterMap.put(handermapping,new HandlerAdapter());
        }
    }

    private void initViewReader(ApplicationContext applicationContext) {
        String templateroot = applicationContext.getReader().getConfig().getProperty(TemplateRoot);
        viewReader = new ViewReader(templateroot);


    }

    private void initHandleMapings(ApplicationContext applicationContext) {
        String[] beanNames = applicationContext.getBeanNames();
        for(String beanName : beanNames){
            Object instance = applicationContext.getInstanceBybean(beanName);
            Class<?> clazz = instance.getClass();

            String baseurl = null;
            if(clazz.isAnnotationPresent(TRequestMapping.class)){
                baseurl = clazz.getAnnotation(TRequestMapping.class).value().trim();
            }
            for(Method method : clazz.getMethods()){
                if(! method.isAnnotationPresent(TRequestMapping.class)){continue;}
                String childurl = method.getAnnotation(TRequestMapping.class).value().trim()
                                        .replaceAll("\\*",".*");
                String url = baseurl  + "/" +childurl;
                url = url.replaceAll("/+","/");
                Pattern p = Pattern.compile(url);

                urlPattenList.add(new UrlPattenMapping(p,instance,method));
            }

        }
    }

    //post方法还没完成
    @Override
    public void doPost(ChannelHandlerContext ctx, THttpResquest resquest, THttpResponse response) {
        String url = resquest.getUrl();


    }

    @Override
    public void doGet(ChannelHandlerContext ctx, THttpResquest resquest, THttpResponse response) {
        String url = resquest.getUrl();
        String contentType = "text/html";
        if(url.endsWith(".css")){
            contentType = "text/css";
        }else if(url.endsWith(".js")){
            contentType = "text/javascript";
        }else if(url.toLowerCase().matches(".*\\.(jpg|png|gif)$")) {
            String img =url.substring(url.lastIndexOf(".")+1);
            contentType = "image/"+img;
        }else if(url.endsWith(".ico")){
            System.out.println("请求ico");
            return;
        }
        response.setContentType(contentType);

        try {
            doDispatch(resquest,response);
        } catch (Exception e) {
            proccessResult(resquest,response,new ModelAndView("500"));
            e.printStackTrace();
        }


    }

    private void doDispatch(THttpResquest resquest, THttpResponse response) throws Exception {
        UrlPattenMapping hander = getHander(resquest);
        if(hander == null){
            log.debug("the hander is null \r\n return 404");
            proccessResult(resquest,response,new ModelAndView("404"));
            return;
        }
        HandlerAdapter adapter = handlerAdapterMap.get(hander);
        ModelAndView mv = adapter.handler(resquest,response,hander);
        proccessResult(resquest,response,mv);
    }

    private void proccessResult(THttpResquest resquest, THttpResponse response, ModelAndView mv) {
        String viewName = mv.getViewName();
        View view = viewReader.doRead(viewName);
        view.render(resquest,response,mv.getModel());
    }

    private UrlPattenMapping getHander(THttpResquest resquest) {
        String url=resquest.getUrl().matches("^/(\\?)?(.*)" ) ? "/index.html":resquest.getUrl();
        log.debug("url is "+url);
        if(urlPattenList.isEmpty()){log.debug("urlPattenList.isEmpty"); return  null;}
        for(UrlPattenMapping urlMapping : urlPattenList){
            Matcher matcher = urlMapping.getPattern().matcher(url);
            if(matcher.matches()){
                return urlMapping;
            }
        }
        return  null;
    }
}
