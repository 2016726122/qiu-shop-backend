package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedPayOrder extends DeprecatedAbstractOrderState{

    //引入 Redis，储存订单
    @Autowired
    private RedisCommonProcessor redisProcessor;

    //订单支付完成后的下一个状态：待发货
    @Autowired
    private DeprecatedSendOrder deprecatedSendOrder;

    //订单方法定义——订单支付
    @Override
    protected DeprecatedOrder payOrder(String orderId, DeprecatedOrderContext context) {
        //从 Redis 中取出当前订单，并判断当前订单状态是否为待支付状态
        DeprecatedOrder order = (DeprecatedOrder) redisProcessor.get(orderId);
        if (!order.getState().equals(ORDER_WAIT_PAY)) {
            throw new UnsupportedOperationException("Order state should be ORDER_WAIT_PAY" +
                    ".But now it`s state is : " + order.getState());
        }
        //支付逻辑
        //支付完成后，修改订单状态为待发货，并更新 Redis 缓存
        order.setState(ORDER_WAIT_SEND);
        redisProcessor.set(orderId, order);
        //观察者模式：发送订单支付Event
        super.notifyObserver(orderId, ORDER_WAIT_SEND);
        //订单支付完成，设置Context 上下文角色的 CurrentState 为待发货状态
//        context.setCurrentState(this.deprecatedSendOrder);
        return order;
    }
}
