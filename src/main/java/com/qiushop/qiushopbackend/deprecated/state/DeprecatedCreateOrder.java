package com.qiushop.qiushopbackend.deprecated.state;

import com.qiushop.qiushopbackend.deprecated.DeprecatedConstants;
import com.qiushop.qiushopbackend.deprecated.observer.DeprecatedAbstractObserver;
import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DeprecatedCreateOrder extends DeprecatedAbstractOrderState{

    //引入 Redis，将新生成的订单存放的 Redis
    @Autowired
    private RedisCommonProcessor redisProcessor;

    //订单创建完成后的下一个状态：待支付
//    @Autowired
//    private DeprecatedPayOrder deprecatedPayOrder;

    //订单方法——创建订单
    //修改后 可把第二个参数去掉，保留修改前细节不删除了
    @Override
    protected DeprecatedOrder createOrder(String orderId, String productId, DeprecatedOrderContext context) {
        //创建订单对象，设置状态为 ORDER_WAIT_PAY
        DeprecatedOrder order = DeprecatedOrder.builder()
                .orderId(orderId)
                .productId(productId)
                .state(ORDER_WAIT_PAY)
                .build();
        //将新订单存入 Redis 缓存，15分钟后失效
        redisProcessor.set(orderId, order, 900);
        //观察者模式：发送订单创建Event
        super.notifyObserver(orderId, ORDER_WAIT_PAY);
        //订单创建完成，设置Context 上下文角色的 CurrentState 为待支付状态
//        context.setCurrentState(this.deprecatedPayOrder);
        return order;
    }
}
