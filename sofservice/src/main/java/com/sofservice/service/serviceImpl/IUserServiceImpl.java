package com.sofservice.service.serviceImpl;

import com.sofservice.enty.IUser;
import com.sofservice.service.IUserServer;
import org.springframework.stereotype.Service;

import javax.jws.WebService;

@Service
@WebService
public class IUserServiceImpl implements IUserServer {
    @Override
    public IUser getIUser(Long id) {
        IUser user = new IUser();
        user.setId(id);
        user.setAddress("上海市浦东新区");
        user.setAge(25);
        user.setName("gongj");
        return user;

    }
}
