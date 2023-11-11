package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.bridge.abst.AbstractRegisterLoginComponent;
import com.qiushop.qiushopbackend.bridge.abst.RegisterLoginComponent;
import com.qiushop.qiushopbackend.bridge.abst.factory.RegisterLoginComponentFactory;
import com.qiushop.qiushopbackend.bridge.function.RegisterLoginByDefault;
import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class UserBridgeService {
    @Autowired
    private UserRepository userRepository;

    public String login(String account, String password) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent("Default");
        return component.login(account, password);
    }

    public String register(UserInfo userInfo) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent("Default");
        return component.register(userInfo);
    }

    public String login3rd(HttpServletRequest request, String type) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(type);
        return component.login3rd(request);
    }
}
