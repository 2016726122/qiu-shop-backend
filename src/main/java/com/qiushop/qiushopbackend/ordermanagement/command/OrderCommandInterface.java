package com.qiushop.qiushopbackend.ordermanagement.command;

import com.qiushop.qiushopbackend.pojo.Order;

public interface OrderCommandInterface {
    //执行命令
    void execute(Order order);
}
