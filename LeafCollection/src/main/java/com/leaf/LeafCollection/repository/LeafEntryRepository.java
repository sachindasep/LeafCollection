package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.LeafEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeafEntryRepository extends JpaRepository<LeafEntry, Long> {

    List<LeafEntry> findByEntryDate(LocalDate entryDate);

    List<LeafEntry> findByPartyId(Long partyId);
    //findByPartyIdAndEntryDateBetween()

}