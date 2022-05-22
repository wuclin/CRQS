package com.sofclient8090;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class clientDemo {
    public static void main(String[] args) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:8080/webservice/api?wsdl");
        ObjectMapper mapper = new ObjectMapper();
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            Object[] objects = client.invoke("getIUser", 99L);
            System.out.println(mapper.writeValueAsString(objects[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
