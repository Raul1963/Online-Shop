package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.Location;
import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;

    private final OrderDetailService orderDetailServiceService;

    public List<Revenue> getRevenueByDate(LocalDate date){
        List<Revenue> revenues = revenueRepository.getRevenuesByDate(date);
        if(revenues.isEmpty()){
            throw new ShopException("Revenue not found for date "+date);
        }
        return revenueRepository.getRevenuesByDate(date);
    }

    public void saveAllRevenuesForDay(){
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Object[]> revenuesByLocation = orderDetailServiceService.getRevenueByLocation(yesterday);
        List<Revenue> revenues = new ArrayList<>();
        for(Object[] obj : revenuesByLocation){
            revenues.add(Revenue.builder().date(yesterday).location((Location) obj[0]).salesRevenue(((BigDecimal) obj[1]).doubleValue()).build());
        }

        revenueRepository.saveAll(revenues);
    }

}
