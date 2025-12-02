package ro.msg.learning.shop.util;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.service.RevenueService;

@Component
@RequiredArgsConstructor
public class DailyRevenueScheduler {

    private final RevenueService revenueService;

    @Scheduled(cron = "0 59 23 * * *")
    public void runDailyRevenueScheduler(){
        revenueService.saveAllRevenuesForDay();
    }
}
