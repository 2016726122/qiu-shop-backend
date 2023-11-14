package com.qiushop.qiushopbackend.ordermanagement.command.receiver;

import com.qiushop.qiushopbackend.pojo.Order;
import org.springframework.stereotype.Component;

//只创建一个Receiver，不分开创建了，也可遵从单一职责原则，每个 Receiver 负责不同职责，互不影响
@Component
public class OrderCommandReceiver {

    //接收命令后执行
    public void action(Order order) {
        switch (order.getOrderState()) {
            case ORDER_WAIT_PAY:
                System.out.println("创建订单：order = " + order);
                System.out.println("存入 DB!");
                return;
            case ORDER_WAIT_SEND:
                System.out.println("支付订单：order = " + order);
                System.out.println("存入 DB!");
                System.out.println("通过 queue 通知财务部门");
                System.out.println("通过 queue 通知物流部门");
                return;
            case ORDER_WAIT_RECEIVE:
                System.out.println("订单发货：order = " + order);
                System.out.println("存入 DB!");
                return;
            case ORDER_FINISH:
                System.out.println("接收订单：order = " + order);
                System.out.println("存入 DB!");
                return;
            default:
                throw new UnsupportedOperationException("Order state error");
        }
    }
}
