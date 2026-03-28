package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.dto.AccountPrintDto;
import com.leaf.LeafCollection.entity.MonthlyAccount;
import com.leaf.LeafCollection.repository.MonthlyAccountRepository;
import com.leaf.LeafCollection.utils.Status;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@AllArgsConstructor
public class BillingService {
    @Autowired
    private final MonthlyAccountRepository repo;


    public List<AccountPrintDto> getAccountsByBranchAndMonth(Long branchId,
                                                             YearMonth month) {
        List<MonthlyAccount> accounts =
                repo.findByBranchIdAndMonth(branchId, month);
        List<AccountPrintDto> dtoList = accounts.stream().map(a -> {
            AccountPrintDto dto = new AccountPrintDto();
            dto.setId(a.getId());
            dto.setActualPaid(a.getActualPaid());
            dto.setPartyName(a.getParty().getName());
            dto.setPartyCode(a.getParty().getPartyCode());
            dto.setOpeningBalance(a.getOpeningBalance());
            dto.setTotalQuantity(a.getTotalQuantity());
            dto.setRate(a.getRate());
            dto.setTotalAmount(a.getTotalAmount());
            dto.setTotalPayment(a.getTotalPayment());
            dto.setNetBalance(a.getNetBalance());
            dto.setStatus(a.getStatus().name());
            return dto;
        }).toList();

        return dtoList;
    }


    @Transactional
    public void settle(Long id, BigDecimal actualPaid) {

        MonthlyAccount acc = repo.findById(id).orElseThrow();

        if (!Status.FINAL.equals(acc.getStatus())) {
            throw new RuntimeException("Only FINAL bills can be settled");
        }

        BigDecimal netBalance = acc.getNetBalance();

        BigDecimal nextOpening = netBalance.subtract(actualPaid);

        acc.setActualPaid(actualPaid);
        acc.setStatus(Status.PAID);
        acc.setSettledAt(LocalDateTime.now());

        repo.save(acc);

/*        // 🔥 Update next month opening
        YearMonth nextMonth = acc.getMonth().plusMonths(1);

        MonthlyAccount next = repo.findByPartyIdAndMonth(acc.getParty().getId(), nextMonth)
                .orElseGet(() -> new MonthlyAccount());

        next.setOpeningBalance(nextOpening); repo.save(next);*/






    }
}
