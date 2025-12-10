package ro.msg.learning.shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import ro.msg.learning.shop.model.OrderEvent;
import ro.msg.learning.shop.model.OrderStatus;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.NEW)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal().source(OrderStatus.NEW).target(OrderStatus.SAVED).event(OrderEvent.SAVE)
                .and()
                .withExternal().source(OrderStatus.NEW).target(OrderStatus.PLACED).event(OrderEvent.PLACE)
                .and()
                .withExternal().source(OrderStatus.SAVED).target(OrderStatus.CANCELED).event(OrderEvent.CANCEL)
                .and()
                .withExternal().source(OrderStatus.SAVED).target(OrderStatus.PLACED).event(OrderEvent.PLACE)
                .and()
                .withExternal().source(OrderStatus.PLACED).target(OrderStatus.CANCELED).event(OrderEvent.CANCEL);

    }
}
