package com.tribal.crafts.controller;

import com.tribal.crafts.dto.OrderRequest;
import com.tribal.crafts.dto.OrderResponse;
import com.tribal.crafts.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.getOrdersByCustomer(username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/artisan")
    public ResponseEntity<List<OrderResponse>> getArtisanOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.getOrdersByArtisan(username));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.placeOrder(orderRequest, username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.cancelOrder(id, username));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
