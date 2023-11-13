package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedOrderContext {

    @Autowired
    private DeprecatedCreateOrder deprecatedCreateOrder;

    @Autowired
    private DeprecatedPayOrder deprecatedPayOrder;

    @Autowired
    private DeprecatedSendOrder deprecatedSendOrder;

    @Autowired
    private DeprecatedReceiveOrder deprecatedReceiveOrder;

    public DeprecatedOrder createOrder(String orderId, String productId) {
        //创建订单，使用 deprecatedCreateOrder
        DeprecatedOrder order = deprecatedCreateOrder.createOrder(orderId, productId, this);
        return order;
    }

    public DeprecatedOrder payOrder(String orderId) {
        //支付订单，使用 deprecatedPayOrder
        DeprecatedOrder order = deprecatedPayOrder.payOrder(orderId, this);
        return order;
    }

    public DeprecatedOrder sendOrder(String orderId) {
        //订单发货，使用 deprecatedSendOrder
        DeprecatedOrder order = deprecatedSendOrder.sendOrder(orderId, this);
        return order;
    }

    public DeprecatedOrder receiveOrder(String orderId) {
        //订单签收，使用 deprecatedReceiveOrder
        DeprecatedOrder order = deprecatedReceiveOrder.receiveOrder(orderId, this);
        return order;
    }

    //修改为无状态的类

//    //引入抽象状态角色，用于状态方法的调用
//    private DeprecatedAbstractOrderState currentState;
//
//    //新创建订单的初始状态
//    //只能同时存在一个订单，删除
//    @Autowired
//    private DeprecatedCreateOrder deprecatedCreateOrder;
//
//    //设置当前的订单状态
//    public void setCurrentState(DeprecatedAbstractOrderState currentState) {
//        this.currentState = currentState;
//    }

//    //创建订单的方法入口，直接调用状态类的 createOrder 方法
//    public DeprecatedOrder createOrder(String orderId, String productId) {
//        this.currentState = this.deprecatedCreateOrder;
//        DeprecatedOrder order = currentState.createOrder(orderId, productId, this);
//        return order;
//    }
//
//    //支付订单的方法入口，直接调用状态类的 payOrder 方法
//    public DeprecatedOrder payOrder(String orderId) {
//        DeprecatedOrder order = currentState.payOrder(orderId, this);
//        return order;
//    }
//
//    //发送订单的方法入口，直接调用状态类的 sendOrder 方法
//    public DeprecatedOrder sendOrder(String orderId) {
//        DeprecatedOrder order = currentState.sendOrder(orderId, this);
//        return order;
//    }
//
//    //接收订单的方法入口， 直接调用状态类的 receiveOrder 方法
//    public DeprecatedOrder receiveOrder(String orderId) {
//        DeprecatedOrder order = currentState.receiveOrder(orderId, this);
//        return order;
//    }

}
