package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.model.ProductLocation;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public Stock findByProductLocation(ProductLocation productLocation){
        return stockRepository.findByProductLocation(productLocation);
    }

    public void saveAll(List<Stock> stocks){
        stockRepository.saveAll(stocks);
    }

    public List<Stock> findMostAbundantStockForProducts(UUID[] productIds){
        return stockRepository.findMostAbundantStockForProducts(productIds);
    }

    public List<Stock> findAll(){
        return stockRepository.findAll();
    }
}
