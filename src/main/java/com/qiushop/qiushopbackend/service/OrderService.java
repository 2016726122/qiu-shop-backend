package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.ordermanagement.command.OrderCommand;
import com.qiushop.qiushopbackend.ordermanagement.command.OrderCommandInvoker;
import com.qiushop.qiushopbackend.ordermanagement.state.OrderState;
import com.qiushop.qiushopbackend.ordermanagement.state.OrderStateChangeAction;
import com.qiushop.qiushopbackend.pojo.Order;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {

    //依赖注入 Spring 状态机，与状态机进行交互
    @Resource
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    //依赖注入 Spring 状态机的 RedisPersister 存取工具，持久化状态机
    @Autowired
    private StateMachinePersister<OrderState, OrderStateChangeAction, String> stateMachineRedisPersister;

    //依赖注入 RedisCommonProcessor，存取订单对象
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private OrderCommand orderCommand;

    //订单创建
    public Order createOrder(String productId) {
        //此处orderId，需要生成全局唯一ID
        String orderId = "OID" + productId;
        //创建订单并存储到 Redis
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .orderState(OrderState.ORDER_WAIT_PAY)
                .build();
        redisCommonProcessor.set(order.getOrderId(), order, 900);
        //调用命令模式
        OrderCommandInvoker invoker = new OrderCommandInvoker();
        invoker.invoke(orderCommand, order);
        return order;
    }

    //订单支付,第三方支付再修改
    public Order pay(String orderId) {
        //从 Redis 中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更 Message， 并附带订单操作 PAY_ORDER
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.PAY_ORDER)
                .setHeader("order", order).build();
        //将 Message 传递给 Spring 状态机
        if (changeStateAction(message,order)) {
            return order;
        }
        return null;
    }

    //订单发送
    public Order send(String orderId) {
        //从 Redis 中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更 Message， 并附带订单操作 SEND_ORDER
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.SEND_ORDER)
                .setHeader("order", order).build();
        //将 Message 传递给 Spring 状态机
        if (changeStateAction(message,order)) {
            return order;
        }
        return null;
    }

    //订单签收
    public Order receive(String orderId) {
        //从 Redis 中获取订单
        Order order = (Order) redisCommonProcessor.get(orderId);
        //包装订单状态变更 Message， 并附带订单操作 RECEIVE_ORDER
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.RECEIVE_ORDER)
                .setHeader("order", order).build();
        //将 Message 传递给 Spring 状态机
        if (changeStateAction(message,order)) {
            return order;
        }
        return null;
    }

    //状态机的相关操作
    private boolean changeStateAction(Message<OrderStateChangeAction> message, Order order) {
        try {
            //启动状态机
            orderStateMachine.start();
            //从 Redis 缓存中读取状态机，缓存的 key 为 orderId+“STATE”，这是自定义的，读者可以根据自己喜好定义
            stateMachineRedisPersister.restore(orderStateMachine, order.getOrderId()+"STATE");
            //将Message 发送给OrderStateListener
            boolean res = orderStateMachine.sendEvent(message);
            //将更改完订单状态的 状态机 存储到 Redis 缓存
            stateMachineRedisPersister.persist(orderStateMachine, order.getOrderId()+"STATE");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return false;
    }

}
