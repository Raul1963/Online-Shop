package ro.msg.learning.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.msg.learning.shop.service.StockLocationSelectionStrategy;

@Configuration
@RequiredArgsConstructor
public class OrderStrategyConfig {

    @Value("${order.stock.strategy:single}")
    private String strategy;

    private final ApplicationContext context;


    @Bean
    public StockLocationSelectionStrategy stockLocationSelectionStrategy() {
        if (strategy.equals("mostabundant")) {
            return context.getBean("mostAbundantLocationStrategy", StockLocationSelectionStrategy.class);
        }
        return context.getBean("singleLocationStrategy", StockLocationSelectionStrategy.class);
    }
}
