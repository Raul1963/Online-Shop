package ro.msg.learning.shop.service;

import org.springframework.stereotype.Service;
import ro.msg.learning.shop.config.StockLocationSelectionStrategy;
import ro.msg.learning.shop.exception.LocationNotFoundException;
import ro.msg.learning.shop.exception.StockNotFoundException;
import ro.msg.learning.shop.model.*;
import ro.msg.learning.shop.repository.LocationRepository;
import ro.msg.learning.shop.repository.ProductRepository;
import ro.msg.learning.shop.repository.StockRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("mostAbundantLocationStrategy")
public class MostAbundantLocationStrategy implements StockLocationSelectionStrategy {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final ProductRepository productRepository;

    public MostAbundantLocationStrategy(StockRepository stockRepository, LocationRepository locationRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderDetail> selectStockLocations(Order order, OrderInformation orderInformation) {
        UUID[] productIds = orderInformation.getProducts().stream().map(ProductQuantity::getProductId).toArray(UUID[]::new);
        List<Stock> mostStockLocations = stockRepository.findMostAbundantStockForProducts(productIds);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for(ProductQuantity productQuantity: orderInformation.getProducts()){
            Product product = productRepository.findById(productQuantity.getProductId()).get();

            Stock selectedStock = mostStockLocations.stream()
                    .filter(stock -> stock.getProductLocation().getProduct().getId().equals(productQuantity.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new StockNotFoundException("No stock found for product " + productQuantity.getProductId()));

            if(productQuantity.getQuantity() > selectedStock.getQuantity()){
                throw new StockNotFoundException("No stock for " + product.getId() + " found");
            }
            OrderDetail orderDetail = OrderDetail.builder()
                    .orderProduct(new OrderProduct(order, product))
                    .location(selectedStock.getProductLocation().getLocation())
                    .quantity(productQuantity.getQuantity())
                    .build();

            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
