package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedSendOrder extends DeprecatedAbstractOrderState{

    //引入 Redis，储存订单
    @Autowired
    private RedisCommonProcessor redisProcessor;

    //订单发货后的下一个状态：待收货
    @Autowired
    private DeprecatedReceiveOrder deprecatedReceiveOrder;

    //订单方法定义——订单发送
    @Override
    protected DeprecatedOrder sendOrder(String orderId, DeprecatedOrderContext context) {
        //从 Redis 中取出当前订单，并判断当前订单状态是否为待发货状态
        DeprecatedOrder order = (DeprecatedOrder) redisProcessor.get(orderId);
        if (!order.getState().equals(ORDER_WAIT_SEND)) {
            throw new UnsupportedOperationException("Order state should be ORDER_WAIT_SEND" +
                    ".But now it`s state is : " + order.getState());
        }
        //支付逻辑
        //点击发货后，修改订单状态为待收货，并更新 Redis 缓存
        order.setState(ORDER_WAIT_RECEIVE);
        redisProcessor.set(orderId, order);
        //观察者模式：发送订单发货 Event
        super.notifyObserver(orderId, ORDER_WAIT_RECEIVE);
        //订单支付完成，设置Context 上下文角色的 CurrentState 为待收货状态
//        context.setCurrentState(this.deprecatedReceiveOrder);
        return order;
    }
}
