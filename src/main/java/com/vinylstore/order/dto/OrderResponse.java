package com.vinylstore.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long productId;
    private String productTitle;
    private Long userId;
    private String userEmail;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total;
    private LocalDateTime fecha;
    private String estado;
}

