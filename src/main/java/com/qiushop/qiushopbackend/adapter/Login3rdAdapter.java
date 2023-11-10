package com.qiushop.qiushopbackend.adapter;

import com.alibaba.fastjson.JSONObject;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import com.qiushop.qiushopbackend.service.UserService;
import com.qiushop.qiushopbackend.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Date;

//通过继承 UserService 复用已有方法，并在此处进行代码扩展，实现第三方账号授权登录功能
@Component
public class Login3rdAdapter extends UserService implements Login3rdTarget{

    @Value("${gitee.state}")
    private String giteeState;
    @Value("${gitee.token.url}")
    private String giteeTokenUrl;
    @Value("${gitee.user.url}")
    private String giteeUserUrl;
    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;

    @Override
    public String loginByGitee(String code, String state) {
        //进行 state 判断，state 的值是前端与后端商定好的，前端将 state 传给 Gitee 平台， Gitee 平台回传 state 给回调接口
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
        //如果第三方账号已经登录过，则直接登录
        if (super.checkUserExists(userName)) {
            return super.login(userName,password);
        }
        //组装用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        //如果第三方账号是第一次登录，先进行自动注册
        super.register(userInfo);
        //自动注册完成后，进行登录
        return super.login(userName,password);
    }

    @Override
    public String loginByWeChat(String... params) {
        return null;
    }

    @Override
    public String loginByQQ(String... params) {
        return null;
    }
}
