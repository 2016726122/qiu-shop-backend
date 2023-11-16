package com.qiushop.qiushopbackend.service.decorator;

import com.qiushop.qiushopbackend.pojo.Order;
import com.qiushop.qiushopbackend.service.inter.OrderServiceInterface;

//实现 OrderServiceInterface
public abstract class AbstractOrderServiceDecorator implements OrderServiceInterface {

    //关联 OrderServiceInterface
    private OrderServiceInterface orderServiceInterface;

    //为 OrderServiceInterface 提供初始化方法
    public void setOrderServiceInterface(OrderServiceInterface orderServiceInterface) {
        this.orderServiceInterface = orderServiceInterface;
    }

    //覆写 createOrder 方法，但不改变方法逻辑，直接调用 orderServiceInterface 的 createOrder 方法
    @Override
    public Order createOrder(String productId) {
        return this.orderServiceInterface.createOrder(productId);
    }

    //覆写 send 方法，但不改变方法逻辑，直接调用 orderServiceInterface 的 send 方法
    @Override
    public Order send(String orderId) {
        return this.orderServiceInterface.send(orderId);
    }

    //覆写 receive 方法，但不改变方法逻辑，直接调用 orderServiceInterface 的 receive 方法
    @Override
    public Order receive(String orderId) {
        return this.orderServiceInterface.receive(orderId);
    }

    //覆写 getPayUrl 方法，但不改变方法逻辑，直接调用 orderServiceInterface 的 getPayUrl 方法
    @Override
    public String getPayUrl(String orderId, Float price, Integer payType) {
        return this.orderServiceInterface.getPayUrl(orderId, price, payType);
    }

    //覆写 pay 方法，但不改变方法逻辑，直接调用 orderServiceInterface 的 pay 方法
    @Override
    public Order pay(String orderId) {
        return this.orderServiceInterface.pay(orderId);
    }

    //定义新的方法，根据 userId 和 productId 更新用户积分、发放红包
    protected abstract void updateScoreAndSendRedPaper(String productId, int serviceLevel, float price);
}
