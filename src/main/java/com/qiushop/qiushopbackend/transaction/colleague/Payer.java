package com.qiushop.qiushopbackend.transaction.colleague;

import com.qiushop.qiushopbackend.transaction.mediator.AbstractMediator;

public class Payer extends AbstractCustomer{

    public Payer(String orderId, AbstractMediator mediator, String customerName) {
        super(orderId, mediator, customerName);
    }

    //覆写与中介者的信息交互方法
    @Override
    public void messageTransfer(String orderId, String targetCustomer, String payResult) {
        //调用中介者的 messageTransfer 方法
        super.mediator.messageTransfer(orderId, targetCustomer, this, payResult);
    }
}
