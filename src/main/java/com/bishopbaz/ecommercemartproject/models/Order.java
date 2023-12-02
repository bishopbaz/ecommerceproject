package com.bishopbaz.ecommercemartproject.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private Long id;
    private String productIds;
    private BigDecimal totalPrice;
}
