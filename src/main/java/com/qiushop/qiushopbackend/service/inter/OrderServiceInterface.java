package com.qiushop.qiushopbackend.service.inter;

import com.qiushop.qiushopbackend.pojo.Order;

public interface OrderServiceInterface {

    Order createOrder(String productId);
    Order pay(String orderId);
    Order send(String orderId);
    Order receive(String orderId);
    String getPayUrl(String orderId, Float price, Integer payType);
}
