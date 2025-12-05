package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.mapper.StockMapper;
import ro.msg.learning.shop.model.Stock;
import ro.msg.learning.shop.service.StockService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping(value = "/export/{locationId}", produces = "text/csv")
    public ResponseEntity<?> exportStock(@PathVariable UUID locationId) {
        List<Stock> stocks = stockService.findStockByLocationId(locationId);

        if (stocks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock not found for locationId: " + locationId);
        }

        return ResponseEntity.status(HttpStatus.OK).body(stocks.stream().map(StockMapper::stockToDto).toList());

    }
}
