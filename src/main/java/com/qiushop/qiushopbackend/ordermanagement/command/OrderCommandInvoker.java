package com.qiushop.qiushopbackend.ordermanagement.command;

import com.qiushop.qiushopbackend.pojo.Order;

public class OrderCommandInvoker {

    public void invoke(OrderCommandInterface command, Order order) {
        //调用命令角色的 execute 方法
        command.execute(order);
    }
}
