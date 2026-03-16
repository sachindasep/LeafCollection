package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.dto.LeafEntryDTO;
import com.leaf.LeafCollection.entity.LeafEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
    
}