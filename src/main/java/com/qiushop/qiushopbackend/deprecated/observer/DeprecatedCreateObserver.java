package com.qiushop.qiushopbackend.deprecated.observer;

import com.qiushop.qiushopbackend.deprecated.DeprecatedConstants;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DeprecatedCreateObserver extends DeprecatedAbstractObserver{

    //将自己添加到 OBSERVER_LIST 中
    @PostConstruct
    public void init() {
        DeprecatedConstants.OBSERVER_LIST.add(this);
    }

    @Override
    public void orderStateHandle(String orderId, String orderState) {
        //订单创建成功后，订单状态必须为待支付状态，否则不予处理
        if (!orderState.equals("ORDER_WAIT_PAY")){
            return;
        }
        //通过命令模式进行后续处理
        System.out.println("监听到：订单创建成功。通过命令模式作后续处理。");
    }
}
