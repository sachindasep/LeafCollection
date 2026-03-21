package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Rate;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.RateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final BranchRepository branchRepository;

    public RateService(RateRepository rateRepository,
                       BranchRepository branchRepository) {
        this.rateRepository = rateRepository;
        this.branchRepository = branchRepository;
    }

    // ✅ Get rate (used in billing)
    public BigDecimal getRate(Long branchId, YearMonth month) {

        return rateRepository.findByBranchIdAndMonth(branchId, month)
                .map(Rate::getRate)
                .orElse(BigDecimal.ZERO); // or throw exception if strict
    }

    // ✅ Create or Update rate
    public Rate saveOrUpdateRate(Long branchId,
                                 YearMonth month,
                                 BigDecimal value) {

        Rate rate = rateRepository
                .findByBranchIdAndMonth(branchId, month)
                .orElse(new Rate());

        if (rate.getId() == null) {
            rate.setBranch(branchRepository.getReferenceById(branchId));
            rate.setMonth(month);
        }

        rate.setRate(value);

        return rateRepository.save(rate);
    }

    // ✅ Optional: strict validation
    public BigDecimal getRateOrThrow(Long branchId, YearMonth month) {

        return rateRepository.findByBranchIdAndMonth(branchId, month)
                .map(Rate::getRate)
                .orElseThrow(() -> new RuntimeException(
                        "Rate not set for " + month));
    }
}