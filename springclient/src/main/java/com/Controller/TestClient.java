package com.Controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class TestClient {
    @RequestMapping("/t1")
    @ResponseBody
    public String get1(){
        return "45";
    }

    @RequestMapping("/client")
    @ResponseBody
    public String get2() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8088/tt");
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity, "UTF-8");
        return "服务器数据" + result;
    }
}
