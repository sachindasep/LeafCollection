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
/*    @Query("""
           SELECT p.partyCode
           FROM Party p
           WHERE p.partyCode LIKE CONCAT(:branchCode, '%')
           ORDER BY p.partyCode DESC
           """)
    Optional<String> findLastCodeByBranch(@Param("branchCode")Long branchCode, Pageable pageable);
 */   Optional<Party> findTopByPartyCodeStartingWithOrderByIdDesc(String branchCode);
}