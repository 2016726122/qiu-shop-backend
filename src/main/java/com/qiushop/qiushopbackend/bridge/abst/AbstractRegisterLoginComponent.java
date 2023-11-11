package com.qiushop.qiushopbackend.bridge.abst;

import com.qiushop.qiushopbackend.bridge.function.RegisterLoginFuncInterface;
import com.qiushop.qiushopbackend.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRegisterLoginComponent {
    //面向接口编程，引入RegisterLoginFuncInterface接口属性。此处为“桥”之所在
    protected RegisterLoginFuncInterface funcInterface;
    //通过有参构造参数，初始化RegisterLoginFuncInterface属性
    public AbstractRegisterLoginComponent(RegisterLoginFuncInterface funcInterface) {
        //校验构造函数入参为 RegisterLoginFuncInterface 类型，且不为 null
        validate(funcInterface);
        this.funcInterface = funcInterface;
    }
    //校验构造函数入参为 RegisterLoginFuncInterface 类型，且不为 null，final 方法，不允许子类覆写
    protected final void validate(RegisterLoginFuncInterface funcInterface) {
        if (!(funcInterface instanceof RegisterLoginFuncInterface)) {
            throw new UnsupportedOperationException("Unknow register/login function type!");
        }
    }

    public abstract String login(String userName, String password);
    public abstract String register(UserInfo userInfo);
    public abstract boolean checkUserExists(String userName);
    public abstract String login3rd(HttpServletRequest request);

}
