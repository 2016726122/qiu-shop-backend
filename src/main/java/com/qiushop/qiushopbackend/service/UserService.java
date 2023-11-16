package com.qiushop.qiushopbackend.service;

import com.qiushop.qiushopbackend.dutychain.AbstractBusinessHandler;
import com.qiushop.qiushopbackend.dutychain.CityHandler;
import com.qiushop.qiushopbackend.dutychain.builder.HandlerEnum;
import com.qiushop.qiushopbackend.mapper.BusinessLaunchRepository;
import com.qiushop.qiushopbackend.mapper.UserRepository;
import com.qiushop.qiushopbackend.pojo.BusinessLaunch;
import com.qiushop.qiushopbackend.pojo.UserInfo;
import com.qiushop.qiushopbackend.ticket.proxy.DirectorProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //注入 BusinessLaunchRepository，用于查询业务投放数据
    @Autowired
    private BusinessLaunchRepository businessLaunchRepository;

    @Autowired
    private DirectorProxy directorProxy;

    //注入 duty.chain
    @Value("${duty.chain}")
    private String handlerType;

    //记录当前 handlerType 的配置，判断 duty.chain 的配置是否有修改
    private String currentHandlerType;

    //记录当前的责任链头节点，如果配置没有修改，下次直接返回即可
    private AbstractBusinessHandler currentHandler;

    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account,password);
        //匹配账号和密码失败，则返回错误信息
        if (userInfo == null) {
            return "account / password ERROR";
        }
        return "Login Success";
    }

    public String register(UserInfo userInfo) {
        //如果账号重复，拒绝注册
        if (checkUserExists(userInfo.getUserName())) {
            throw new RuntimeException("User already registered");
        }
        userInfo.setCreateDate(new Date());
        //save 是 JPA 已有方法，无须自己实现
        userRepository.save(userInfo);
        return "Register Success";
    }

    public boolean checkUserExists(String userName) {
        UserInfo user = userRepository.findByUserName(userName);
        if (user == null) {
            return false;
        }
        return true;
    }

    public List<BusinessLaunch> filterBusinessLaunch(String city, String sex, String product) {
        List<BusinessLaunch> launchList = businessLaunchRepository.findAll();
        return buildChain().processHandler(launchList, city, sex, product);
    }

    private AbstractBusinessHandler buildChain() {
        //如果没有配置，直接返回 null
        if (handlerType == null) {
            return null;
        }
        //如果是第一次配置，将handlerType 记录下来
        if (currentHandlerType == null) {
            this.currentHandlerType = this.handlerType;
        }
        //说明 duty.chain 的配置并未修改且 currentHandler 不为 null，直接返回 currentHandler
        if (this.handlerType.equals(currentHandlerType) && this.currentHandler != null){
            return this.currentHandler;
        } else {
            //说明duty.chain 的配置有修改，需要从新初始化责任链条
            //从新初始化责任链条，需要保证线程安全，仅仅每次修改配置时才会执行一次此处的代码，无性能问题
            System.out.println("配置有修改或首次初始化，组装责任链条！！！");
            synchronized (this) {
                try {
                    //创建哑节点，随意找个具体类型创建即可
                    AbstractBusinessHandler dummyHeadHandler = new CityHandler();
                    //创建前置节点，初始赋值为哑节点
                    AbstractBusinessHandler preHandler = dummyHeadHandler;
                    //将 duty.chain 的配置用逗号分割为List 类型，并通过 HandlerEnum 创建责任类，并配置责任链条
                    List<String> handlerTypeList = Arrays.asList(handlerType.split(","));
                    for (String handlerType : handlerTypeList) {
                        AbstractBusinessHandler handler = (AbstractBusinessHandler) Class.forName(HandlerEnum.valueOf(handlerType).getValue()).newInstance();
                        preHandler.nextHandler = handler;
                        preHandler = handler;
                    }
                    //重新赋值新的责任链头节点
                    this.currentHandler = dummyHeadHandler.nextHandler;
                    //重新赋值修改后的配置
                    this.currentHandlerType = this.handlerType;
                    //返回责任链头节点
                    return currentHandler;
                } catch (Exception e) {
                    throw new UnsupportedOperationException(e);
                }
            }
        }
    }

    public Object createTicket(String type, String productId, String content, String title, String bankInfo, String taxId) {
        return directorProxy.buildTicket(type, productId, content, title, bankInfo, taxId);
    }
}
