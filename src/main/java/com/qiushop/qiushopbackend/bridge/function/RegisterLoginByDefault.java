package com.qiushop.qiushopbackend.bridge.function;

import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class RegisterLoginByDefault implements RegisterLoginFuncInterface {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account,password);
        //匹配账号和密码失败，则返回错误信息
        if (userInfo == null) {
            return "account / password ERROR";
        }
        return "Login Success";
    }

    @Override
    public String register(UserInfo userInfo) {
        //如果账号重复，拒绝注册
        if (checkUserExists(userInfo.getUserName())) {
            throw new RuntimeException("User already registered");
        }
        userInfo.setCreateDate(new Date());
        //save 是 JPA 已有方法，无须自己实现
        userRepository.save(userInfo);
        return "Register Success";
    }

    @Override
    public boolean checkUserExists(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user == null) {
            return false;
        }
        return true;
    }

    //瑕疵所在，RegisterLoginByDefault 不需要实现 login3rd 方法
    @Override
    public String login3rd(HttpServletRequest request) {
        return null;
    }
}
