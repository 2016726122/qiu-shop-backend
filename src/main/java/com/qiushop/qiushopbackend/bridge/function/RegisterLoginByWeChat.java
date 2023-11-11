package com.qiushop.qiushopbackend.bridge.function;

import com.qiushop.qiushopbackend.bridge.abst.factory.RegisterLoginComponentFactory;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@Component
public class RegisterLoginByWeChat extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface {

    @Override
    public String login3rd(HttpServletRequest request) {
        return null;
    }

    @PostConstruct
    private void initFuncMap() {
        RegisterLoginComponentFactory.funcMap.put("WeChat",this);
    }
}
