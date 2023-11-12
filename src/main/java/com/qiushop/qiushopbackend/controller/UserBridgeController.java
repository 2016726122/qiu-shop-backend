package com.qiushop.qiushopbackend.controller;

import com.qiushop.qiushopbackend.pojo.UserInfo;
import com.qiushop.qiushopbackend.service.UserBridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/bridge")//不能与其他接口重名
public class UserBridgeController {

    //引入 UserBridgeService 依赖
    @Autowired
    private UserBridgeService userBridgeService;

    @PostMapping("/login")
    public String login(String account, String password){
        return userBridgeService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userBridgeService.register(userInfo);
    }

    //参数调整为 HttpServletRequest
    @GetMapping("/gitee")
    public String gitee(HttpServletRequest request) throws IOException {
        return userBridgeService.login3rd(request, "GITEE");
    }
}
