package com.qiushop.qiushopbackend.pay.strategy.context;

import com.qiushop.qiushopbackend.pay.strategy.PayStrategyInterface;
import com.qiushop.qiushopbackend.pojo.Order;

public class PayContext extends AbstractPayContext{

    //关联抽象策略类
    private PayStrategyInterface payStrategyInterface;
    //设计具体策略
    public PayContext(PayStrategyInterface payStrategy) {
        this.payStrategyInterface = payStrategy;
    }
    //执行策略
    @Override
    public String execute(Order order) {
        return this.payStrategyInterface.pay(order);
    }
}
