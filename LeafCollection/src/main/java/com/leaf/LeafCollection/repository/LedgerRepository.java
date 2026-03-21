package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LedgerRepository extends JpaRepository<LedgerEntry, Long> {

    Optional<LedgerEntry> findTopByPartyIdOrderByEntryDateDescIdDesc(Long partyId);

    List<LedgerEntry> findByPartyIdOrderByEntryDateAscIdAsc(Long partyId);
}