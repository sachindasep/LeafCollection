package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.service.RateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.YearMonth;
@Controller
@RequestMapping("/rate")
public class RateController {

    private final RateService rateService;
    private final BranchRepository branchRepository;

    public RateController(RateService rateService,
                          BranchRepository branchRepository) {
        this.rateService = rateService;
        this.branchRepository = branchRepository;
    }

    // ✅ Open page
    @GetMapping
    public String ratePage(Model model) {
        model.addAttribute("branches", branchRepository.findAll());
        return "rate";
    }

    // ✅ Load existing rate
    @GetMapping("/load")
    public String loadRate(@RequestParam Long branchId,
                           @RequestParam String month,
                           Model model) {

        YearMonth ym = YearMonth.parse(month);

        BigDecimal rate = rateService.getRate(branchId, ym);

        model.addAttribute("branches", branchRepository.findAll());
        model.addAttribute("selectedBranch", branchId);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("existingRate", rate);

        return "rate";
    }

    // ✅ Save rate
    @PostMapping("/save")
    public String saveRate(@RequestParam Long branchId,
                           @RequestParam String month,
                           @RequestParam BigDecimal rate) {

        rateService.saveOrUpdateRate(
                branchId,
                YearMonth.parse(month),
                rate
        );

        return "redirect:/rate/load?branchId=" + branchId + "&month=" + month;
    }
}