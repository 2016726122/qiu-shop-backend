package com.qiushop.qiushopbackend.pay.facade;

import com.qiushop.qiushopbackend.pay.strategy.AlipayStrategy;
import com.qiushop.qiushopbackend.pay.strategy.WechatStrategy;
import com.qiushop.qiushopbackend.pay.strategy.context.PayContext;
import com.qiushop.qiushopbackend.pay.strategy.factory.PayContextFactory;
import com.qiushop.qiushopbackend.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayFacade {

    @Autowired
    private PayContextFactory contextFactory;

    public String pay(Order order, Integer payType) {
        //获取 PayContext
        PayContext context = contextFactory.getContext(payType);
        //调用支付方法
        return context.execute(order);
    }
}
