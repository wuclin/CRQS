package com.sofservice8081.controller;

import com.sofservice8081.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class tController {

    @Autowired
    UserService userService;

    @RequestMapping("t1")
    @ResponseBody
    public String get1(){
        return userService.get();
    }
}
