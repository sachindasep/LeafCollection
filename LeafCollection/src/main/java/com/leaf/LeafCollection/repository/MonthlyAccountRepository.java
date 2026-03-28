package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.MonthlyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface MonthlyAccountRepository extends JpaRepository<MonthlyAccount, Long> {
    Optional<MonthlyAccount> findByPartyIdAndMonth(Long partyId, YearMonth month);
    List<MonthlyAccount> findByBranchIdAndMonth(Long branchId, YearMonth month);
    @Query("""
    SELECT m FROM MonthlyAccount m
    WHERE m.party.id = :partyId
      AND m.month = :prevMonth
""")
    Optional<MonthlyAccount> findPreviousMonth(Long partyId, YearMonth prevMonth);
    void deleteByBranchIdAndMonth(Long branchId, YearMonth month);
    @Query("""
    SELECT COUNT(m) > 0 FROM MonthlyAccount m
    WHERE m.branch.id = :branchId
      AND m.month = :month
      AND m.status = 'FINAL'
""")
    boolean isFinalized(Long branchId, YearMonth month);
    @Modifying
    @Query("DELETE FROM MonthlyAccount m WHERE m.branch.id = :branchId AND m.month = :month AND m.status = 'DRAFT'")
    void deleteDrafts(Long branchId, YearMonth month);

    @Query("SELECT DISTINCT m.party.partyCode FROM MonthlyAccount m WHERE m.branch.id = :branchId AND m.month = :month AND m.status != 'DRAFT' ")
    List<String> findPartyIdsNotInDraft(Long branchId, YearMonth month);
}
