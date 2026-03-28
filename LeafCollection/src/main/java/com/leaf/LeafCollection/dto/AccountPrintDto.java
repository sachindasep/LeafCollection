package com.leaf.LeafCollection.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountPrintDto {

    private Long id;
    private String partyName;
    private String partyCode;

    private BigDecimal openingBalance;
    private BigDecimal totalQuantity;
    private BigDecimal rate;
    private BigDecimal totalAmount;
    private BigDecimal totalPayment;
    private BigDecimal netBalance;
    private String status;
    private BigDecimal actualPaid;

    // getters & setters
}
