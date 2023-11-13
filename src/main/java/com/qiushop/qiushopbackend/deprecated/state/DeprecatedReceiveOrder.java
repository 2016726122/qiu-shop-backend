package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedReceiveOrder extends DeprecatedAbstractOrderState{

    //引入 Redis，储存订单
    @Autowired
    private RedisCommonProcessor redisProcessor;

    //订单方法定义——订单签收
    @Override
    protected DeprecatedOrder receiveOrder(String orderId, DeprecatedOrderContext context) {
        //从 Redis 中取出当前订单，并判断当前订单状态是否为待收付状态
        DeprecatedOrder order = (DeprecatedOrder) redisProcessor.get(orderId);
        if (!order.getState().equals(ORDER_WAIT_RECEIVE)) {
            throw new UnsupportedOperationException("Order state should be ORDER_WAIT_RECEIVE" +
                    ".But now it`s state is : " + order.getState());
        }
        //用户收货后，修改订单状态为订单完成状态，并删除 Redis 缓存
        order.setState(ORDER_FINISH);
        //观察者模式：发送订单收获 Event
        super.notifyObserver(orderId, ORDER_FINISH);
        redisProcessor.remove(orderId);
        return order;
    }
}
