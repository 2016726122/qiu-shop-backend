package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.ordermanagement.audit.*;
import com.qiushop.qiushopbackend.ordermanagement.command.OrderCommand;
import com.qiushop.qiushopbackend.ordermanagement.command.OrderCommandInvoker;
import com.qiushop.qiushopbackend.ordermanagement.state.OrderState;
import com.qiushop.qiushopbackend.ordermanagement.state.OrderStateChangeAction;
import com.qiushop.qiushopbackend.pay.facade.PayFacade;
import com.qiushop.qiushopbackend.pojo.Order;
import com.qiushop.qiushopbackend.service.inter.OrderServiceInterface;
import com.qiushop.qiushopbackend.transaction.colleague.AbstractCustomer;
import com.qiushop.qiushopbackend.transaction.colleague.Buyer;
import com.qiushop.qiushopbackend.transaction.colleague.Payer;
import com.qiushop.qiushopbackend.transaction.mediator.Mediator;
import com.qiushop.qiushopbackend.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class OrderService implements OrderServiceInterface {

    //依赖注入 Spring 状态机，与状态机进行交互
    @Resource
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    //依赖注入 Spring 状态机的 RedisPersister 存取工具，持久化状态机
    @Autowired
    private StateMachinePersister<OrderState, OrderStateChangeAction, String> stateMachineRedisPersister;

    //依赖注入 RedisCommonProcessor，存取订单对象
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    //引入命令模式类
    @Autowired
    private OrderCommand orderCommand;

    //引入门面
    @Autowired
    private PayFacade payFacade;

    @Autowired
    private Mediator mediator;

    @Autowired
    private CreateOrderLog createOrderLog;

    @Autowired
    private PayOrderLog payOrderLog;

    @Autowired
    private SendOrderLog sendOrderLog;

    @Autowired
    private ReceiveOrderLog receiveOrderLog;

    //订单创建
    @Override
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
        //创建订单审计日志
        createOrderLog.createAuditLog("用户信息","创建订单",orderId);
        //发送订单日志到queue 略
        return order;
    }

    //订单支付
    @Override
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
        payOrderLog.createAuditLog("用户信息","支付订单",orderId);
        OrderAuditLog orderAuditLog = new OrderAuditLog();
        payOrderLog.buildDetails(orderAuditLog);
        return null;
    }

    //订单发送
    @Override
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
        sendOrderLog.createAuditLog("用户信息", "订单发货",orderId);
        OrderAuditLog orderAuditLog = new OrderAuditLog();
        sendOrderLog.buildDetails(orderAuditLog);
        return null;
    }

    //订单签收
    @Override
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
        receiveOrderLog.createAuditLog("用户信息", "订单签收", orderId);
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

    @Override
    public String getPayUrl(String orderId, Float price, Integer payType) {
        Order order = (Order) redisCommonProcessor.get(orderId);
        order.setPrice(price);
        return payFacade.pay(order,payType);
    }

    public void friendPay(String sourceCustomer, String orderId, String targetCustomer, String payResult, String role) {
        //创建中介者
//        Mediator mediator = new Mediator();
        Buyer buyer = new Buyer(orderId, mediator, sourceCustomer);
        Payer payer = new Payer(orderId, mediator, sourceCustomer);
//        mediator.setBuyer(buyer);
//        mediator.setPayer(payer);
        HashMap<String, AbstractCustomer> map = new HashMap<>();
        map.put("buyer", buyer);
        map.put("payer", payer);
        //将同事类配置到 customerInstances 中
        mediator.customerInstances.put(orderId, map);
        if (role.equals("B")) {
            buyer.messageTransfer(orderId, targetCustomer, payResult);
        } else if (role.equals("P")) {
            payer.messageTransfer(orderId, targetCustomer, payResult);
        }
    }

}
