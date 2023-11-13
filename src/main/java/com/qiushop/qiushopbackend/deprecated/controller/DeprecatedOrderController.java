package com.qiushop.qiushopbackend.deprecated.controller;

import com.qiushop.qiushopbackend.deprecated.pojo.DeprecatedOrder;
import com.qiushop.qiushopbackend.deprecated.service.DeprecatedOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deprecated/order")
public class DeprecatedOrderController {

    //注入 DeprecatedOrderService
    @Autowired
    private DeprecatedOrderService deprecatedOrderService;

    //订单创建
    @PostMapping("/create")
    public DeprecatedOrder createOrder(@RequestParam String productId) {
        return deprecatedOrderService.createOrder(productId);
    }

    //订单支付
    @PostMapping("/pay")
    public DeprecatedOrder payOrder(@RequestParam String orderId) {
        return deprecatedOrderService.pay(orderId);
    }

    //订单发送
    @PostMapping("/send")
    public DeprecatedOrder send(@RequestParam String orderId) {
        return deprecatedOrderService.send(orderId);
    }

    //订单签收
    @PostMapping("/receive")
    public DeprecatedOrder receive(@RequestParam String orderId) {
        return deprecatedOrderService.receive(orderId);
    }
}
