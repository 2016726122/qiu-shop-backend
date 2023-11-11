package com.qiushop.qiushopbackend.bridge.function;

import com.qiushop.qiushopbackend.bridge.abst.factory.RegisterLoginComponentFactory;
import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class RegisterLoginByDefault extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(String account, String password) {
       return super.commonLogin(account,password,userRepository);
    }

    @Override
    public String register(UserInfo userInfo) {
        return super.commonRegister(userInfo,userRepository);
    }

    @Override
    public boolean checkUserExists(String userName) {
        return super.commonCheckUserExists(userName,userRepository);
    }

    @PostConstruct
    private void initFuncMap() {
        RegisterLoginComponentFactory.funcMap.put("Default",this);
    }

}
