package com.qiushop.qiushopbackend.pay.strategy.factory;

public abstract class AbstractPayContextFactory<T> {

    public abstract T getContext(Integer payType);
}
