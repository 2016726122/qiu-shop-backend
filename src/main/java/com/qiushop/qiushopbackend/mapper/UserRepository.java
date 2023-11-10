package com.qiushop.qiushopbackend.mapper;

import com.qiushop.qiushopbackend.pojo.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    //根据用户名查询用户信息
    UserInfo findByUserName(String userName);
    //根据用户名和密码查询用户信息
    UserInfo findByUserNameAndUserPassword(String account, String password);
}
