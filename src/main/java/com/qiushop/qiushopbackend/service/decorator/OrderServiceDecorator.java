package com.qiushop.qiushopbackend.service.decorator;

import com.qiushop.qiushopbackend.mapper.ProductsRepository;
import com.qiushop.qiushopbackend.pojo.Order;
import com.qiushop.qiushopbackend.pojo.Products;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceDecorator extends AbstractOrderServiceDecorator{

    //引入 Apollo 配置中心的消息超时时间
    @Value("${delay.service.time}")
    private String delayServiceTime;
    //注入 ProductsRepository，查询商品信息
    @Autowired
    private ProductsRepository productsRepository;
    //注入 RabbitTemplate，发送消息
    @Resource
    private RabbitTemplate rabbitTemplate;

    //覆写 OrderServiceDecorator 中的抽象方法，具体实现积分更新和红包发放
    @Override
    protected void updateScoreAndSendRedPaper(String productId, int serviceLevel, float price) {
        switch (serviceLevel) {
            case 0:
                //根据价格的百分之一更新积分
                int score = Math.round(price) / 100;
                System.out.println("正常处理，为用户更新积分！score = " + score);
                //根据商品属性发放红包
                Products product = productsRepository.findByProductId(productId);
                if (product != null && product.getSendRedBag() == 1) {
                    System.out.println("正常处理，为用户发放红包！ productId = " + productId);
                }
                break;
            case 1:
                MessageProperties properties = new MessageProperties();
                //设置消息过期时间
                properties.setExpiration(delayServiceTime);
                Message msg = new Message(productId.getBytes(),properties);
                //向正常队列中发送消息
                rabbitTemplate.send("normalExchange", "myRKey", msg);
                System.out.println("延迟处理，时间= "+delayServiceTime);
                break;
            case 2:
                System.out.println("暂停服务!");
                break;
            default:
                throw new UnsupportedOperationException("不支持的服务级别！");
        }
    }

    //将 pay 方法与 updateScoreAndSendRedPaper 方法进行逻辑结合
    public Order decoratorPay(String orderId, int serviceLevel, float price) {
        //调用原有 OrderService 的逻辑
        Order order = super.pay(orderId);
        //新的逻辑，更新积分，发放用户红包
        try {
            this.updateScoreAndSendRedPaper(order.getProductId(), serviceLevel, price);
        } catch (Exception e) {
            //重试机制。此处积分更新不能影响支付主流程
        }
        return order;
    }
}
