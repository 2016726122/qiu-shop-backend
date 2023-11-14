package com.qiushop.qiushopbackend.ordermanagement.statemachine;

import com.qiushop.qiushopbackend.ordermanagement.state.OrderState;
import com.qiushop.qiushopbackend.ordermanagement.state.OrderStateChangeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import javax.annotation.Resource;
import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "orderStateMachine")
@SuppressWarnings("all")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {

    //覆写 config 方法，进行状态机的初始化操作
    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
        states.withStates().initial(OrderState.ORDER_WAIT_PAY)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal()
                //从 OREDER_WAIT_PAY 状态转化为 ORDER_WAIT_SEND 状态，需要 PAY_ORDER 操作
                .source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_WAIT_SEND)
                .event(OrderStateChangeAction.PAY_ORDER)
                .and()
        //从 OREDER_WAIT_SEND 状态转化为 ORDER_WAIT_RECEIVE 状态，需要 SEND_ORDER 操作
                .withExternal().source(OrderState.ORDER_WAIT_SEND)
                .target(OrderState.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeAction.SEND_ORDER)
                .and()
        //从 OREDER_WAIT_RECEIVE 状态转化为 ORDER_FINISH 状态，需要 RECEIVE_ORDER 操作
                .withExternal().source(OrderState.ORDER_WAIT_RECEIVE)
                .target(OrderState.ORDER_FINISH)
                .event(OrderStateChangeAction.RECEIVE_ORDER);
    }
    
    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<OrderState, OrderStateChangeAction> getRedisPersister(RedisConnectionFactory redisConnectionFactory) {

        RedisStateMachineContextRepository<OrderState, OrderStateChangeAction> repository
                = new RedisStateMachineContextRepository<>(redisConnectionFactory);
        RepositoryStateMachinePersist persister 
                = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(persister);
    }


}
