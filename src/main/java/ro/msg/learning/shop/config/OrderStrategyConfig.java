package ro.msg.learning.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderStrategyConfig {

    @Value("${order.stock.strategy:single}")
    private String strategy;

    public final ApplicationContext context;

    public OrderStrategyConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public StockLocationSelectionStrategy stockLocationSelectionStrategy() {
        switch (strategy) {
            case "single":
                return context.getBean("singleLocationStrategy",StockLocationSelectionStrategy.class);
            default:
                return context.getBean("singleLocationStrategy", StockLocationSelectionStrategy.class);
        }
    }
}
