package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPartyIdAndPaymentDateBetween(
            Long partyId, LocalDate start, LocalDate end
    );

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.party.id = :partyId
      AND p.paymentDate <= :endDate
""")
    BigDecimal getTotalAdvanceTillDate(Long partyId, LocalDate endDate);
    @Query("""
    SELECT p FROM Payment p
    WHERE p.paymentDate = :date
      AND p.status = 'ACTIVE'
    ORDER BY p.id DESC
""")
    List<Payment> findTodayPayments(LocalDate date);
    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.paymentDate = :date
      AND p.status = 'ACTIVE'
""")
    BigDecimal getTodayTotal(LocalDate date);
    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.party.id = :partyId
      AND p.paymentDate BETWEEN :startDate AND :endDate
      AND p.status = 'ACTIVE'
""")
    BigDecimal getTotalPayment(Long partyId, LocalDate startDate, LocalDate endDate);
    @Query("""
    SELECT p FROM Payment p
    JOIN p.party party
    JOIN party.branch b
    WHERE p.paymentDate BETWEEN :startDate AND :endDate
      AND (:branchId IS NULL OR b.id = :branchId)
      AND p.status = 'ACTIVE'
    ORDER BY p.paymentDate DESC, p.id DESC
""")
    List<Payment> findPaymentsByDateRangeAndBranch(LocalDate startDate, LocalDate endDate, Long branchId);

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    JOIN p.party party
    JOIN party.branch b
    WHERE p.paymentDate BETWEEN :startDate AND :endDate
      AND (:branchId IS NULL OR b.id = :branchId)
      AND p.status = 'ACTIVE'
""")
    BigDecimal getTotalByDateRangeAndBranch(LocalDate startDate, LocalDate endDate, Long branchId);
}
