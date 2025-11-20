package com.vinylstore.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "El título es obligatorio")
    private String title;
    
    @NotBlank(message = "El artista es obligatorio")
    private String artist;
    
    @NotBlank(message = "El género es obligatorio")
    private String genre;
    
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private BigDecimal price;
    
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String imageUrl;
}

