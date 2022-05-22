package com.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.net.www.http.HttpClient;

@Controller
public class userController {

    @RequestMapping("/tt")
    @ResponseBody
    public String get1(){
        return "123";
    }


    @RequestMapping("/t2")
    @ResponseBody
    public String get2(){
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:8084/webservice/api?wsdl");
        String detail = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            Object[] objects = client.invoke("getIUser", 99L);
            detail = mapper.writeValueAsString(objects[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @RequestMapping("/t4")
    @ResponseBody
    public String t4(){

        return null;
    }
}
