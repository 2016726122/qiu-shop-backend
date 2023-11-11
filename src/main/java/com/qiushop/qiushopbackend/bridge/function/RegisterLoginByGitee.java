package com.qiushop.qiushopbackend.bridge.function;

import com.alibaba.fastjson.JSONObject;
import com.qiushop.qiushopbackend.bridge.abst.factory.RegisterLoginComponentFactory;
import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import com.qiushop.qiushopbackend.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class RegisterLoginByGitee extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface {

    @Value("${gitee.state}")
    private String giteeState;
    @Value("${gitee.token.url}")
    private String giteeTokenUrl;
    @Value("${gitee.user.url}")
    private String giteeUserUrl;
    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login3rd(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        if (!giteeState.equals(state)) {
            throw new UnsupportedOperationException("Invalid state!");
        }
        //请求 Gitee 平台获取 Token，并携带 code
        String tokenUrl = giteeTokenUrl.concat(code);
        JSONObject tokenResponse = HttpClientUtils.execute(tokenUrl, HttpMethod.POST);
        String token = String.valueOf(tokenResponse.get("access_token"));
        //请求用户信息，并携带 Token
        String userUrl = giteeUserUrl.concat(token);
        JSONObject userInfoResponse = HttpClientUtils.execute(userUrl, HttpMethod.GET);
        //获取用户信息， userName 添加前缀 GITEE@， 密码保持与 userName 一致
        String userName = giteeUserPrefix.concat(String.valueOf(userInfoResponse.get("name")));
        String password = userName;
        //自动注册和登录功能，此处体现了方法的“复用”
        return autoRegister3rdAndLogin(userName,password);
    }

    private String autoRegister3rdAndLogin(String userName, String password) {
        //如果第三方账号已经登录过，则直接登录. 此处“代码复用”
        if (super.commonCheckUserExists(userName,userRepository)) {
            return login(userName,password);
        }
        //组装用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        //如果第三方账号是第一次登录，先进行自动注册。 此处“代码复用”
        super.commonRegister(userInfo,userRepository);
        //自动注册完成后，进行登录。 此处“代码复用”
        return super.commonLogin(userName,password,userRepository);
    }

    @PostConstruct
    private void initFuncMap() {
        RegisterLoginComponentFactory.funcMap.put("GITEE",this);
    }
}
