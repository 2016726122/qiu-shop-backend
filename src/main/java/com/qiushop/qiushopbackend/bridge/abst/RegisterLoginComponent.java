package com.qiushop.qiushopbackend.bridge.abst;

import com.qiushop.qiushopbackend.bridge.function.RegisterLoginFuncInterface;
import com.qiushop.qiushopbackend.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public class RegisterLoginComponent extends AbstractRegisterLoginComponent{
    //通过构造参数，传入“桥梁” RegisterLoginFuncInterface 的具体类型
    public RegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        super(funcInterface);
    }

    @Override
    protected String login(String userName, String password) {
        //直接通过桥梁，调用右路 Implementor 的方法即可，把具体实现交给右路的实现类
        return funcInterface.login(userName,password);
    }

    @Override
    protected String register(UserInfo userInfo) {
        return funcInterface.register(userInfo);
    }

    @Override
    protected boolean checkUserExists(String userName) {
        return funcInterface.checkUserExists(userName);
    }

    @Override
    protected String login3rd(HttpServletRequest request) {
        return funcInterface.login3rd(request);
    }
}
