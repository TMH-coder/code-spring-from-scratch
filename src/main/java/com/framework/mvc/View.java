package com.framework.mvc;

import com.Server.Http.THttpResponse;
import com.Server.Http.THttpResquest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

@Slf4j
public class View {
    private File viewFile;
    public View(File viewFile) {
        this.viewFile = viewFile;
    }
    void render(THttpResquest resquest, THttpResponse response, Map<String,?> model){
        response.write(viewFile);
    }
}
