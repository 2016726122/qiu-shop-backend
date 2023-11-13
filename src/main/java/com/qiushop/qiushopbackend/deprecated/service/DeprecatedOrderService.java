package com.qiushop.qiushopbackend.deprecated.service;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.deprecated.state.DeprecatedOrderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeprecatedOrderService {

    @Autowired
    private DeprecatedOrderContext orderContext;

    public DeprecatedOrder createOrder(String productId) {
        //订单 ID 的生成逻辑
        String orderId = "OID" + productId;
        return orderContext.createOrder(orderId, productId);
    }

    //以下三个方法的代码，与Context 的代码差不多
    public DeprecatedOrder pay(String orderId) {
        return orderContext.payOrder(orderId);
    }

    public DeprecatedOrder send(String orderId) {
        return orderContext.sendOrder(orderId);
    }

    public DeprecatedOrder receive(String orderId) {
        return orderContext.receiveOrder(orderId);
    }
}
