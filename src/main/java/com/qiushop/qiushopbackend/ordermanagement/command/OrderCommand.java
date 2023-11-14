package com.qiushop.qiushopbackend.ordermanagement.command;

import com.qiushop.qiushopbackend.ordermanagement.command.receiver.OrderCommandReceiver;
import com.qiushop.qiushopbackend.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCommand implements OrderCommandInterface{

    //注入命令接收者
    @Autowired
    private OrderCommandReceiver receiver;

    @Override
    public void execute(Order order) {
        //调用命令接收者的 action 方法，执行命令
        this.receiver.action(order);
    }
}
