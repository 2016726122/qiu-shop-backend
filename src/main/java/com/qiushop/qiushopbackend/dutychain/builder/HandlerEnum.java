package com.qiushop.qiushopbackend.dutychain.builder;

public enum HandlerEnum {

    //业务投放目的城市
    city("com.qiushop.qiushopbackend.dutychain.CityHandler"),
    //业务投放性别群体
    sex("com.qiushop.qiushopbackend.dutychain.SexHandler"),
    //业务投放相关产品
    product("com.qiushop.qiushopbackend.dutychain.ProductHandler");

    String value = "";

    HandlerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
