package com.qiushop.qiushopbackend.ticket.director;

import com.qiushop.qiushopbackend.ticket.builder.CompanyTicketBuilder;
import com.qiushop.qiushopbackend.ticket.builder.PersonalTicketBuilder;
import org.springframework.stereotype.Component;

@Component
public class Director extends AbstractDirector{
    @Override
    public Object buildTicket(String type, String product, String content, String title, String bankInfo, String taxId) {
        //个人电子发票
        if (type.equals("person")) {
            PersonalTicketBuilder personalTicketBuilder = new PersonalTicketBuilder();
            personalTicketBuilder.setCommonInfo(title, product, content);
            return personalTicketBuilder.buildTicket();
        }
        //企业电子发票
        else if (type.equals("company")) {
            CompanyTicketBuilder companyTicketBuilder = new CompanyTicketBuilder();
            companyTicketBuilder.setCommonInfo(title, product, content);
            companyTicketBuilder.setTaxId(taxId);
            companyTicketBuilder.setBankInfo(bankInfo);
            return companyTicketBuilder.buildTicket();
        }
        throw new UnsupportedOperationException("不支持的发票类型！");
    }
}
