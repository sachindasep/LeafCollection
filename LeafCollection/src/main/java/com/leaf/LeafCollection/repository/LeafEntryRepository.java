package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.dto.LeafEntryDTO;
import com.leaf.LeafCollection.dto.LeafReportDTO;
import com.leaf.LeafCollection.entity.LeafEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeafEntryRepository extends JpaRepository<LeafEntry, Long> {

    List<LeafEntry> findByEntryDate(LocalDate entryDate);

    List<LeafEntry> findByPartyId(Long partyId);
    
    @Query("SELECT new com.leaf.LeafCollection.dto.LeafEntryDTO(" +
           "le.id, " +
           "p.partyCode, " +
           "b.name, " +
           "p.name, " +
           "le.quantity) " +
           "FROM LeafEntry le " +
           "JOIN le.party p " +
           "JOIN p.branch b " +
           "WHERE le.entryDate = CURRENT_DATE " +
           "ORDER BY le.createdAt DESC")
    List<LeafEntryDTO> getTodaysLeafEntries();
    @Query("""
                SELECT new com.leaf.LeafCollection.dto.LeafReportDTO(
                    l.id,
                    l.entryDate,
                    b.name,
                    p.partyCode,
                    p.name,
                    SUM(l.quantity)
                )
                FROM LeafEntry l
                JOIN l.party p
                JOIN p.branch b
                WHERE l.entryDate = :date
                  AND (:branchCode IS NULL OR b.branchCode = :branchCode)
                GROUP BY b.branchCode, p.partyCode, p.name, l.id, l.entryDate
                ORDER BY p.partyCode
            """)
    List<LeafReportDTO> getLeafReport(
            @Param("date") LocalDate date,
            @Param("branchCode") String branchCode
    );

    @Query("""
                SELECT new com.leaf.LeafCollection.dto.LeafReportDTO(
                    l.id,
                    l.entryDate,
                    b.name,
                    p.partyCode,
                    p.name,
                    SUM(l.quantity)
                )
                FROM LeafEntry l
                JOIN l.party p
                JOIN p.branch b
                WHERE l.entryDate BETWEEN :startDate AND :endDate
                  AND (:branchCode IS NULL OR b.branchCode = :branchCode)
                GROUP BY b.branchCode, p.partyCode, p.name, l.id, l.entryDate
                ORDER BY p.partyCode
            """)
    List<LeafReportDTO> getLeafReport(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("branchCode") String branchCode
    );
    @Query("""
    SELECT COALESCE(SUM(l.quantity), 0)
    FROM LeafEntry l
    WHERE l.party.id = :partyId
      AND l.entryDate BETWEEN :startDate AND :endDate
""")
    BigDecimal getTotalQuantity(Long partyId, LocalDate startDate,  LocalDate endDate);

}