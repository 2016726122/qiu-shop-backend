package com.qiushop.qiushopbackend.controller;

import com.qiushop.qiushopbackend.adapter.Login3rdAdapter;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import com.qiushop.qiushopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Login3rdAdapter login3rdAdapter;

    @PostMapping("/login")
    public String login(String account, String password){
        return userService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userService.register(userInfo);
    }

    @GetMapping("/gitee")
    public String gitee(String code, String state) throws IOException {
        return login3rdAdapter.loginByGitee(code,state);
    }
}
