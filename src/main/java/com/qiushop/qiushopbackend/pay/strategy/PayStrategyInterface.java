package com.qiushop.qiushopbackend.pay.strategy;

import com.qiushop.qiushopbackend.pojo.Order;

public interface PayStrategyInterface {

    //定义公共的支付方法
    String pay(Order order);
}
