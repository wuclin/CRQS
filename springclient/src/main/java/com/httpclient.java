package com;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class httpclient {
    public static void testGetNoParams() throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8088/tt");
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity, "UTF-8");
        //return "服务器数据" + result;

    }
}
