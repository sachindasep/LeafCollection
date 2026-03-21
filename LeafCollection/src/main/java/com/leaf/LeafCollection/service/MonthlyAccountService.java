package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.entity.MonthlyAccount;
import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.repository.LeafEntryRepository;
import com.leaf.LeafCollection.repository.MonthlyAccountRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import com.leaf.LeafCollection.repository.PaymentRepository;
import com.leaf.LeafCollection.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@Data
public class MonthlyAccountService {

    private final MonthlyAccountRepository monthlyRepo;
    private final PartyRepository partyRepo;
    private final LeafEntryRepository leafRepo;
    private final PaymentRepository paymentRepo;
    private final RateService rateService;

    public MonthlyAccountService(MonthlyAccountRepository monthlyRepo,
                                 PartyRepository partyRepo,
                                 LeafEntryRepository leafRepo,
                                 PaymentRepository paymentRepo,
                                 RateService rateService) {
        this.monthlyRepo = monthlyRepo;
        this.partyRepo = partyRepo;
        this.leafRepo = leafRepo;
        this.paymentRepo = paymentRepo;
        this.rateService = rateService;
    }

    @Transactional
    public void generateMonthlyBill(Long branchId, YearMonth month) {

        // ❗ Check finalized
        if (monthlyRepo.isFinalized(branchId, month)) {
           // throw new RuntimeException("Already finalized. Cannot regenerate."); //TODO
            return;
        }

        // 🧹 Delete old (regenerate)
        monthlyRepo.deleteByBranchIdAndMonth(branchId, month);

        List<Party> parties = partyRepo.findByBranchId(branchId);

        String monthStr = month.toString(); // "2026-03"
        //TODO move to Util
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();
        for (Party party : parties) {

            // 1. Quantity
            BigDecimal qty = leafRepo.getTotalQuantity(
                    party.getId(), startDate,endDate);

            // 2. Rate
            BigDecimal rate = rateService.getRate(branchId, month);

            BigDecimal totalAmount = qty.multiply(rate);

            // 3. Payment
            BigDecimal payment = paymentRepo.getTotalPayment(
                    party.getId(), startDate,endDate);

            // 4. Opening Balance
            BigDecimal openingBalance = getOpeningBalance(party.getId(), month);

            // 5. Net Balance
            BigDecimal netBalance = openingBalance
                    .add(totalAmount)
                    .subtract(payment);

            // 6. Save
            MonthlyAccount acc = new MonthlyAccount();
            acc.setParty(party);
            acc.setBranch(party.getBranch()); // or fetch if needed
            acc.setMonth(month);

            acc.setTotalQuantity(qty);
            acc.setRate(rate);
            acc.setTotalAmount(totalAmount);

            acc.setTotalPayment(payment);
            acc.setOpeningBalance(openingBalance);
            acc.setNetBalance(netBalance);

            acc.setStatus(Status.DRAFT);
            acc.setLastCalculatedAt(LocalDateTime.now());

            monthlyRepo.save(acc);
        }
    }

    public BigDecimal getOpeningBalance(Long partyId, YearMonth month) {

        YearMonth prev = month.minusMonths(1);

        return monthlyRepo.findByPartyIdAndMonth(partyId, prev)
                .map(MonthlyAccount::getNetBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Transactional
    public void finalizeMonth(Long branchId, YearMonth month) {

        List<MonthlyAccount> list =
                monthlyRepo.findByBranchIdAndMonth(branchId, month);

        for (MonthlyAccount acc : list) {
            acc.setStatus(Status.FINAL);
        }

        monthlyRepo.saveAll(list);
    }
}
