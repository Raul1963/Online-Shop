package ro.msg.learning.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.msg.learning.shop.exception.ShopException;
import ro.msg.learning.shop.mapper.RevenueMapper;
import ro.msg.learning.shop.model.Revenue;
import ro.msg.learning.shop.service.RevenueService;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/revenues")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService revenueService;

    @PreAuthorize("hasRole('COSTUMER')")
    @GetMapping("/revenue/{date}")
    public ResponseEntity<?> getRevenue(@PathVariable LocalDate date) {
        try {
            List<Revenue> revenues = revenueService.getRevenueByDate(date);

            return ResponseEntity.ok(revenues.stream().map(RevenueMapper::toDto).toList());
        }
        catch (ShopException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
