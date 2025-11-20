package com.vinylstore.cart.service;

import com.vinylstore.cart.dto.*;
import com.vinylstore.cart.model.CartItem;
import com.vinylstore.cart.repository.CartItemRepository;
import com.vinylstore.product.model.Product;
import com.vinylstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartService {
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    public List<CartItemResponse> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public CartItemResponse addItem(Long userId, CartItemRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        
        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setUnitPrice(product.getPrice());
        }
        
        cartItem = cartItemRepository.save(cartItem);
        return mapToResponse(cartItem);
    }
    
    public CartItemResponse updateItemQuantity(Long userId, Long itemId, UpdateQuantityRequest request) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));
        
        if (!cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para modificar este item");
        }
        
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente");
        }
        
        cartItem.setQuantity(request.getQuantity());
        cartItem = cartItemRepository.save(cartItem);
        
        return mapToResponse(cartItem);
    }
    
    public void deleteItem(Long userId, Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item del carrito no encontrado"));
        
        if (!cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("No autorizado para eliminar este item");
        }
        
        cartItemRepository.deleteById(itemId);
    }
    
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
    
    public CartTotalResponse getCartTotal(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        
        List<CartItemResponse> itemResponses = items.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        BigDecimal total = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Integer totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        
        return new CartTotalResponse(userId, itemResponses, total, totalItems);
    }
    
    private CartItemResponse mapToResponse(CartItem cartItem) {
        BigDecimal subtotal = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getUserId(),
                cartItem.getProductId(),
                cartItem.getQuantity(),
                cartItem.getUnitPrice(),
                subtotal,
                cartItem.getCreatedAt(),
                cartItem.getUpdatedAt()
        );
    }
}

