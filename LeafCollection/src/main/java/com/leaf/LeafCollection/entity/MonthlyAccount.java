package com.leaf.LeafCollection.entity;

import com.leaf.LeafCollection.utils.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"party_id", "branch_id", "month"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    // 2026-03
    @Column(name = "month_year", nullable = false)
    private YearMonth month;

    // Leaf data
    @Column(name = "total_quantity", precision = 12, scale = 2)
    private BigDecimal totalQuantity = BigDecimal.ZERO;

    @Column(name = "rate", precision = 12, scale = 2)
    private BigDecimal rate = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // Payment data
    @Column(name = "total_payment", precision = 14, scale = 2)
    private BigDecimal totalPayment = BigDecimal.ZERO;

    // Carry forward
    @Column(name = "opening_balance", precision = 14, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(name = "net_balance", precision = 14, scale = 2)
    private BigDecimal netBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.DRAFT;

    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt;

    // Optional audit
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private BigDecimal actualPaid;   // amount entered during settlement
    private LocalDateTime settledAt;
}
