package com.qiushop.qiushopbackend.transaction.mediator;

import com.qiushop.qiushopbackend.transaction.colleague.AbstractCustomer;

public abstract class AbstractMediator {

    public abstract void messageTransfer(String orderId, String targetCustomer, AbstractCustomer customer, String payResult);
}
