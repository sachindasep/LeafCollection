package com.leaf.LeafCollection.repository;

import com.leaf.LeafCollection.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findByBranchIdAndMonth(Long branchId, YearMonth month);

}
