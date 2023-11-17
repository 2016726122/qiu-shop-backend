package com.qiushop.qiushopbackend.transaction.colleague;

import com.qiushop.qiushopbackend.transaction.mediator.AbstractMediator;

public abstract class AbstractCustomer {

    //关联中介者对象
    public AbstractMediator mediator;

    //订单 ID
    public String orderId;

    //当前用户信息
    public String customerName;

    //构造函数
    AbstractCustomer(String orderId, AbstractMediator mediator, String customerName) {
        this.mediator = mediator;
        this.orderId = orderId;
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    //定义与中介者的信息交互方法，供子类进行实现
    public abstract void messageTransfer(String orderId, String targetCustomer, String payResult);
}
