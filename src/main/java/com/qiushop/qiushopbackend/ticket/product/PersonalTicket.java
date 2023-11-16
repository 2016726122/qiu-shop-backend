package com.qiushop.qiushopbackend.ticket.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalTicket implements Cloneable{

    //发票固定不变的消息
    private String finalInfo;

    //发票抬头
    private String title;

    //商品信息
    private String product;

    //税率，发票代码，校验码，收款方等信息
    private String content;

    @Override
    public PersonalTicket clone() {
        PersonalTicket personalTicket = null;
        try {
            personalTicket = (PersonalTicket) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return personalTicket;
    }
}
