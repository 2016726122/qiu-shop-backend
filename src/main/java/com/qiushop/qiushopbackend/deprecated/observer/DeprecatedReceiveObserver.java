package com.qiushop.qiushopbackend.deprecated.observer;

import com.qiushop.qiushopbackend.deprecated.DeprecatedConstants;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DeprecatedReceiveObserver extends DeprecatedAbstractObserver{

    //将自己添加到 OBSERVER_LIST 中
    @PostConstruct
    public void init() {
        DeprecatedConstants.OBSERVER_LIST.add(this);
    }

    @Override
    public void orderStateHandle(String orderId, String orderState) {
        if (!orderState.equals("ORDER_FINISH")){
            return;
        }
        //通过命令模式进行后续处理
        System.out.println("监听到：订单签收成功。通过命令模式作后续处理。");
    }
}
