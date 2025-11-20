package com.vinylstore.cart.controller;

import com.vinylstore.cart.dto.*;
import com.vinylstore.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Long userId) {
        List<CartItemResponse> items = cartService.getCart(userId);
        return ResponseEntity.ok(items);
    }
    
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartItemResponse> addItem(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequest request) {
        CartItemResponse item = cartService.addItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }
    
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateQuantityRequest request) {
        CartItemResponse item = cartService.updateItemQuantity(userId, itemId, request);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long userId,
            @PathVariable Long itemId) {
        cartService.deleteItem(userId, itemId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{userId}/total")
    public ResponseEntity<CartTotalResponse> getCartTotal(@PathVariable Long userId) {
        CartTotalResponse total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(total);
    }
}

