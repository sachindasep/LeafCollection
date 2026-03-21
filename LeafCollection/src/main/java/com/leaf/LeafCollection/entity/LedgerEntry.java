package com.leaf.LeafCollection.entity;

import com.leaf.LeafCollection.utils.LedgerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Party (main accounting owner)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;


    // Entry date (very important for ordering & monthly calc)
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    // Type of transaction
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LedgerType type;

    // Reference (link to source table)
    @Column(name = "reference_id")
    private Long referenceId;

    // Credit = company owes party (leaf collection value)
    @Column(name = "credit", precision = 12, scale = 2)
    private BigDecimal credit = BigDecimal.ZERO;

    // Debit = party owes company (advance payment)
    @Column(name = "debit", precision = 12, scale = 2)
    private BigDecimal debit = BigDecimal.ZERO;

    // Running balance after this entry
    @Column(name = "balance", precision = 12, scale = 2)
    private BigDecimal balance;

    // Optional notes
    @Column(name = "remarks")
    private String remarks;

    // Audit
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}