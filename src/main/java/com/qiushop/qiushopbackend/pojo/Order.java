package com.qiushop.qiushopbackend.pojo;

import com.qiushop.qiushopbackend.ordermanagement.state.OrderState;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {

    private String orderId;

    private String productId;

    private OrderState orderState;  //订单状态

    private Float price;            //商品价格
}
