package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Object> findByBranchCode(String branchCode);
}