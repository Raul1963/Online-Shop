package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.repository.RevenueRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public List<Revenue> getRevenueByDate(LocalDate date){
        List<Revenue> revenues = revenueRepository.getRevenuesByDate(date);
        if(revenues.isEmpty()){
            throw new ShopException("Revenue not found for date "+date);
        }
        return revenueRepository.getRevenuesByDate(date);
    }

}
