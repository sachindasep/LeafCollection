package com.leaf.LeafCollection.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SettlementRequest {
    private Long id;
    private BigDecimal amount;
}