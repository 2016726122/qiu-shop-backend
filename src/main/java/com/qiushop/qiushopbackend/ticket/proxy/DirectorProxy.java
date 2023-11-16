package com.qiushop.qiushopbackend.ticket.proxy;

import com.qiushop.qiushopbackend.ticket.director.AbstractDirector;
import com.qiushop.qiushopbackend.ticket.director.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class DirectorProxy extends AbstractDirector {
    //关联被代理角色
    @Autowired
    private Director director;

    @Override
    public Object buildTicket(String type, String productId, String content, String title, String bankInfo, String taxId) {
        //前置处理：通过 productId 获取商品信息
        String product = this.getProduct(productId);
        //前置处理，校验银行卡信息
        if (bankInfo != null && !this.validateBankInfo(bankInfo)) {
            return null;
        }
        //代理 director 类的 buildTicket 方法
        return director.buildTicket(type, product, content, title, bankInfo, taxId);
    }

    //前置处理：通过 productId 获取商品信息
    private String getProduct(String productId) {
        return "通过 productId 获取商品信息";
    }

    //前置处理：校验银行卡信息
    private boolean validateBankInfo(String bankInfo) {
        System.out.println("银行卡校验逻辑！");
        return true;
    }
}
