package com.qiushop.qiushopbackend.bridge.function;

import com.qiushop.qiushopbackend.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public class RegisterLoginByWeChat implements RegisterLoginFuncInterface {

    @Override
    public String login(String account, String password) {
        return null;
    }

    @Override
    public String register(UserInfo userInfo) {
        return null;
    }

    @Override
    public boolean checkUserExists(String userName) {
        return false;
    }

    @Override
    public String login3rd(HttpServletRequest request) {
        return null;
    }
}
