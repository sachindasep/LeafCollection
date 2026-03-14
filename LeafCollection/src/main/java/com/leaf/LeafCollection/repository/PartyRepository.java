package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findByBranchId(Long branchId);

}