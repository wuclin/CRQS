package com.sofservice8081.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Reference
    IUservice iUservice;

    public String get(){
        return iUservice.getIUser();
    }
}
