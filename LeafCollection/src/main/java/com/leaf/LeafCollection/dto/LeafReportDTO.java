package com.leaf.LeafCollection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafReportDTO {

    private Long id;
    private String branchName;
    private String partyCode;
    private String partyName;
    private java.math.BigDecimal quantity;


}