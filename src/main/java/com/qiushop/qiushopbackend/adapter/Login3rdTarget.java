package com.qiushop.qiushopbackend.adapter;

public interface Login3rdTarget {

    String loginByGitee(String code, String state);

    String loginByWeChat(String... params);

    String loginByQQ(String... params);
}
