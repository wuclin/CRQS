package com.service.serviceImpl;

import com.service.HelloService;
import org.annotation.RpcProvider;

@RpcProvider
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello" + name;
    }
}
