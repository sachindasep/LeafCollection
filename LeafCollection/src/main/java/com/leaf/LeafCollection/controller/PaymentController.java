package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import com.leaf.LeafCollection.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PartyRepository partyRepository;

    public PaymentController(PaymentService paymentService,
                             PartyRepository partyRepository) {
        this.paymentService = paymentService;
        this.partyRepository = partyRepository;
    }

    @GetMapping("/new")
    public String showPaymentForm(Model model) {
        model.addAttribute("parties", partyRepository.findAll());
      //  model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("todayPayments", paymentService.getTodayPayments());
        model.addAttribute("todayTotal", paymentService.getTodayTotal());
        return "payments";
    }

    @PostMapping("/save")
    public String savePayment(@RequestParam Long partyId,
                              @RequestParam String paymentDate,
                              @RequestParam BigDecimal amount,
                              @RequestParam(required = false) String paymentMode,
                              @RequestParam(required = false) String remarks) {

        paymentService.savePayment(
                partyId,
                LocalDate.parse(paymentDate),
                amount,
                paymentMode,
                remarks
        );

        return "redirect:/payments/new?success";
    }
}