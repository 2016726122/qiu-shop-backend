package com.qiushop.qiushopbackend.pay.strategy.context;

import com.qiushop.qiushopbackend.pojo.Order;

public abstract class AbstractPayContext {
    public abstract String execute(Order order);
}
