package com.leaf.LeafCollection.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeafReportDTO {

    private Long id;
    private LocalDate entryDate;
    private String branchName;
    private String partyCode;
    private String partyName;
    private java.math.BigDecimal quantity;


}