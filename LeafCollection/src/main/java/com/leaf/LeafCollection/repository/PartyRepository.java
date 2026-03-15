package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.Party;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findByBranchId(Long branchId);

    Optional<Party> findTopByPartyCodeStartingWithOrderByIdDesc(String branchCode);
}