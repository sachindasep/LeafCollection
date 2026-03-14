package com.leaf.LeafCollection.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "parties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String address;

    private String upi;

    private String accountNumber;

    private String ifsc;

    private Boolean active = true;

    private LocalDateTime createdAt;

    @Column(unique = true, nullable = false)
    private String partyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

}