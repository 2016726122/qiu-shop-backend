package com.qiushop.qiushopbackend.bridge.function;

import com.qiushop.qiushopbackend.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public interface RegisterLoginFuncInterface {

    //以下三个方法的创建，我们已经感受到了“重构”的强烈气息，需要把 UserService 的逻辑移到该类的子类中
    public String login(String account, String password);
    public String register(UserInfo userInfo);
    public boolean checkUserExists(String userName);
    //第三方登录账号登录接口
    public String login3rd(HttpServletRequest request);
}
