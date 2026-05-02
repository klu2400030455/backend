package com.tribal.crafts.controller;

import com.tribal.crafts.dto.CartRequest;
import com.tribal.crafts.dto.CartResponse;
import com.tribal.crafts.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartResponse>> getCart(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.getCartByCustomerEmail(username));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody CartRequest cartRequest, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(cartService.addToCart(cartRequest, username));
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable Long id, @RequestBody CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.updateQuantity(id, cartRequest.getQuantity()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        String username = authentication.getName();
        cartService.clearCart(username);
        return ResponseEntity.ok().build();
    }
}
