package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.entity.Payment;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import com.leaf.LeafCollection.service.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PartyRepository partyRepository;
    private final BranchRepository branchRepository;

    public PaymentController(PaymentService paymentService,
                             PartyRepository partyRepository,
                             BranchRepository branchRepository) {
        this.paymentService = paymentService;
        this.partyRepository = partyRepository;
        this.branchRepository = branchRepository;
    }

    @GetMapping
    public String showPaymentForm(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                  @RequestParam(required = false) Long branchId,
                                  Model model) {
        // Default to today if no dates provided
        if (startDate == null) startDate = LocalDate.now();
        if (endDate == null) endDate = LocalDate.now();

        List<Payment> payments = paymentService.getPayments(startDate, endDate, branchId);
        BigDecimal total = paymentService.getTotalPayments(startDate, endDate, branchId);

        model.addAttribute("payment", new Payment());
        model.addAttribute("parties", partyRepository.findAll());
        model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("payments", payments);
        model.addAttribute("total", total);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedBranchId", branchId);
        model.addAttribute("isEditMode", false);
        return "payments";
    }

    @GetMapping("/edit/{id}")
    public String editPayment(@PathVariable Long id,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              @RequestParam(required = false) Long branchId,
                              Model model) {
        Payment payment = paymentService.findById(id);
        List<Payment> payments = paymentService.getPayments(startDate != null ? startDate : LocalDate.now(),
                                                            endDate != null ? endDate : LocalDate.now(),
                                                            branchId);
        BigDecimal total = paymentService.getTotalPayments(startDate != null ? startDate : LocalDate.now(),
                                                           endDate != null ? endDate : LocalDate.now(),
                                                           branchId);

        model.addAttribute("payment", payment);
        model.addAttribute("parties", partyRepository.findAll());
        model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("payments", payments);
        model.addAttribute("total", total);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedBranchId", branchId);
        model.addAttribute("isEditMode", true);
        return "payments";
    }

    @GetMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                @RequestParam(required = false) Long branchId) {
        paymentService.deletePayment(id);
        String redirectUrl = "/payments?startDate=" + (startDate != null ? startDate : LocalDate.now()) +
                             "&endDate=" + (endDate != null ? endDate : LocalDate.now());
        if (branchId != null) {
            redirectUrl += "&branchId=" + branchId;
        }
        return "redirect:" + redirectUrl;
    }

    @PostMapping("/save")
    public String savePayment(@ModelAttribute Payment payment) {
        if (payment.getId() == null) {
            // New payment
            paymentService.savePayment(
                payment.getParty().getId(),
                payment.getPaymentDate(),
                payment.getAmount(),
                payment.getPaymentMode() != null ? payment.getPaymentMode().name() : null,
                payment.getRemarks()
            );
        } else {
            // Update payment
            paymentService.updatePayment(payment);
        }
        return "redirect:/payments";
    }
}