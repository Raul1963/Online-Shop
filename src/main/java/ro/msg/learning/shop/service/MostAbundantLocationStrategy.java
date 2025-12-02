package ro.msg.learning.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("mostAbundantLocationStrategy")
@RequiredArgsConstructor
public class MostAbundantLocationStrategy implements StockLocationSelectionStrategy {
    private final StockService stockService;
    private final ProductService productService;

    @Override
    public List<OrderDetail> selectStockLocations(Order order) {
        UUID[] productIds = order.getOrderDetails().stream().map(orderDetail -> orderDetail.getOrderProduct().getProduct().getId()).toArray(UUID[]::new);
        List<Stock> mostStockLocations = stockService.findMostAbundantStockForProducts(productIds);

        List<OrderDetail> orderDetails = new ArrayList<>();

        for(OrderDetail orderDetail: order.getOrderDetails()){
            Product product = productService.readById(orderDetail.getOrderProduct().getProduct().getId());

            Stock selectedStock = mostStockLocations.stream()
                    .filter(stock -> stock.getProductLocation().getProduct().getId().equals(orderDetail.getOrderProduct().getProduct().getId()))
                    .findFirst()
                    .orElseThrow(() -> new ShopException("No stock found for product " + orderDetail.getOrderProduct().getProduct().getId()));

            if(orderDetail.getQuantity() > selectedStock.getQuantity()){
                throw new ShopException("Not sufficient stock for " + product.getName() + " found");
            }
            orderDetail.setLocation(selectedStock.getProductLocation().getLocation());
            orderDetail.setOrderProduct(new OrderProduct(order,product));

            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
