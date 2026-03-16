package com.leaf.LeafCollection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafEntryDTO {
    
    private Long id;
    private String partyCode;
    private String branchName;
    private String partyName;
    private BigDecimal weight;
    
}

