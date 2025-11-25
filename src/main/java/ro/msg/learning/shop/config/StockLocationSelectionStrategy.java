package ro.msg.learning.shop.config;

import ro.msg.learning.shop.model.Order;
import ro.msg.learning.shop.model.OrderDetail;
import ro.msg.learning.shop.model.OrderInformation;

import java.util.List;

public interface StockLocationSelectionStrategy {
    List<OrderDetail> selectStockLocations(Order order,OrderInformation orderInformation);
}
