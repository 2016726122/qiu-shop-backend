package com.qiushop.qiushopbackend.pay.strategy;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qiushop.qiushopbackend.pojo.Order;
import com.qiushop.qiushopbackend.utils.Constants;

//支付宝策略类
public class AlipayStrategy implements PayStrategyInterface{
    @Override
    public String pay(Order order) {
        //创建 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(Constants.ALIPAY_GATEWAY, Constants.APP_ID, Constants.APP_PRIVATE_KEY,
                "JSON","UTF-8", Constants.ALIPAY_PUBLIC_KEY, Constants.SIGN_TYPE);
        //设置请求参数
        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        payRequest.setReturnUrl(Constants.CALLBACK_URL);
        payRequest.setBizContent("{\"out_trade_no\":\"" + order.getOrderId() + "\","
                + "\"total_amount\":\"" + order.getPrice() + "\","
                + "\"subject\":\"" + "商品信息" + "\","
                + "\"body\":\"" + "商品描述" + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        try {
            String result = alipayClient.pageExecute(payRequest,"GET").getBody();
            return result;
        } catch (Exception e) {
            throw new UnsupportedOperationException("Alipay failed! " + e);
        }
    }
}
