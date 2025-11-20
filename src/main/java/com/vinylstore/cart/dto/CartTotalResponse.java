package com.vinylstore.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartTotalResponse {
    private Long userId;
    private List<CartItemResponse> items;
    private BigDecimal total;
    private Integer totalItems;
}

