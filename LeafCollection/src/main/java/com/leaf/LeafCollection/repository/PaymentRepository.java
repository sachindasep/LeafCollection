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
}
