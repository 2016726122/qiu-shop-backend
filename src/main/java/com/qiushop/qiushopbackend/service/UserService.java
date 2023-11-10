package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account,password);
        //匹配账号和密码失败，则返回错误信息
        if (userInfo == null) {
            return "account / password ERROR";
        }
        return "Login Success";
    }

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

    public boolean checkUserExists(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user == null) {
            return false;
        }
        return true;
    }
}
