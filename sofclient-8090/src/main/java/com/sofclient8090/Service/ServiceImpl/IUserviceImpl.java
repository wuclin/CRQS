package com.sofclient8090.Service.ServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofclient8090.Service.IUservice;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service
@Component
public class IUserviceImpl implements IUservice {
    @Override
    public String getIUser() {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient("http://localhost:8080/webservice/api?wsdl");
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
}
