package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.DeprecatedConstants;
import com.qiushop.qiushopbackend.deprecated.observer.DeprecatedAbstractObserver;
import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;

import java.util.List;
import java.util.Vector;

public abstract class DeprecatedAbstractOrderState {

    //订单状态定义， 待支付，待发货，待收货，订单完成
    protected final String ORDER_WAIT_PAY = "ORDER_WAIT_PAY";
    protected final String ORDER_WAIT_SEND = "ORDER_WAIT_SEND";
    protected final String ORDER_WAIT_RECEIVE = "ORDER_WAIT_RECEIVE";
    protected final String ORDER_FINISH = "ORDER_FINISH";

    //关联抽象观察者。以 list 的形式进行关联，因为要支持观察者的添加和移除操作
    protected final List<DeprecatedAbstractObserver> observerList = DeprecatedConstants.OBSERVER_LIST;

    //订单方法定义——创建订单
    protected DeprecatedOrder createOrder(String orderId, String productId, DeprecatedOrderContext context) {
        throw new UnsupportedOperationException();
    }

    //订单方法定义——订单支付
    protected DeprecatedOrder payOrder(String orderId, DeprecatedOrderContext context) {
        throw new UnsupportedOperationException();
    }

    //订单方法定义——订单发送
    protected DeprecatedOrder sendOrder(String orderId, DeprecatedOrderContext context) {
        throw new UnsupportedOperationException();
    }

    //订单方法定义——订单签收
    protected DeprecatedOrder receiveOrder(String orderId, DeprecatedOrderContext context) {
        throw new UnsupportedOperationException();
    }

    //新增观察者
    public void addObserver(DeprecatedAbstractObserver observer) {
        this.observerList.add(observer);
    }

    //移除观察者
    public void removeObserver(DeprecatedAbstractObserver observer) {
        this.observerList.remove(observer);
    }

    //通知观察者进行相关操作，并调用 orderStateHandle 方法
    public void notifyObserver(String orderId, String orderState) {
        for (DeprecatedAbstractObserver observer : this.observerList) {
            observer.orderStateHandle(orderId, orderState);
        }
    }
}
