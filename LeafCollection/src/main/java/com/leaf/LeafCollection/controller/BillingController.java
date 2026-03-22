package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.MonthlyAccountRepository;
import com.leaf.LeafCollection.service.MonthlyAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.YearMonth;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final MonthlyAccountService service;
    private final MonthlyAccountRepository repo;
    private final BranchRepository branchRepo;

    public BillingController(MonthlyAccountService service,
                             MonthlyAccountRepository repo,
                             BranchRepository branchRepo) {
        this.service = service;
        this.repo = repo;
        this.branchRepo = branchRepo;
    }

    @GetMapping
    public String billingPage(Model model) {
        model.addAttribute("branches", branchRepo.findAll());
        return "billing";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam Long branchId,
                           @RequestParam String month,
                           Model model) {

        YearMonth ym = YearMonth.parse(month);

        service.generateMonthlyBill(branchId, ym);
        generateUiData(branchId, ym, model);

        model.addAttribute("accounts",
                repo.findByBranchIdAndMonth(branchId, ym));

        model.addAttribute("selectedBranch", branchId);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedBranchName", branchRepo.findById(branchId).map(b -> b.getName()).orElse(""));
        model.addAttribute("branches", branchRepo.findAll());

        return "billing";
    }

    private void generateUiData(Long branchId, YearMonth ym, Model model) {
        java.util.List<com.leaf.LeafCollection.entity.MonthlyAccount> accounts= repo.findByBranchIdAndMonth(branchId, ym);
        BigDecimal totalOpening = accounts.stream()
                .map(a -> a.getOpeningBalance() == null ? BigDecimal.ZERO : a.getOpeningBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalQty = accounts.stream()
                .map(a -> a.getTotalQuantity() == null ? BigDecimal.ZERO : a.getTotalQuantity())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = accounts.stream()
                .map(a -> a.getTotalAmount() == null ? BigDecimal.ZERO : a.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPayment = accounts.stream()
                .map(a -> a.getTotalPayment() == null ? BigDecimal.ZERO : a.getTotalPayment())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalNet = accounts.stream()
                .map(a -> a.getNetBalance() == null ? BigDecimal.ZERO : a.getNetBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalOpening", totalOpening);
        model.addAttribute("totalQty", totalQty);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalPayment", totalPayment);
        model.addAttribute("totalNet", totalNet);
    }

    @PostMapping("/finalize")
    public String finalize(@RequestParam Long branchId,
                           @RequestParam String month) {

        service.finalizeMonth(branchId, YearMonth.parse(month));

        return "redirect:/billing";
    }
}
