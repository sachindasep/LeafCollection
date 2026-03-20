package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.dto.LeafEntryDTO;
import com.leaf.LeafCollection.dto.LeafReportDTO;
import com.leaf.LeafCollection.entity.LeafEntry;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.service.LeafEntryService;
import com.leaf.LeafCollection.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/leaf-entry")
public class LeafEntryController {

    @Autowired
    private LeafEntryService leafEntryService;

    @Autowired
    private PartyService partyService;
    @Autowired
    private BranchRepository branchRepository;

    @GetMapping
    public String leafEntryPage(Model model){

        model.addAttribute("leafEntry", new LeafEntry());
        model.addAttribute("parties", partyService.getAllParties());
        model.addAttribute("todaysList",leafEntryService.getTodaysLeafEntries());
        model.addAttribute("todaysTotalWeight", leafEntryService.getTodaysTotalWeight());
        model.addAttribute("isEditMode", false);

        return "leaf-entry";
    }

    @PostMapping("/save")
    public String saveLeafEntry(@ModelAttribute LeafEntry leafEntry){

        leafEntryService.save(leafEntry);

        return "redirect:/leaf-entry";
    }

    @GetMapping("/today")
    @ResponseBody
    public List<LeafEntryDTO> getTodaysLeafEntries(){
        return leafEntryService.getTodaysLeafEntries();
    }

    @GetMapping("/delete/{id}")
    public String deleteLeafEntry(@PathVariable Long id){
        leafEntryService.deleteById(id);
        return "redirect:/leaf-entry";
    }
    
    @GetMapping("/edit/{id}")
    public String editLeafEntry(@PathVariable Long id, Model model){
        LeafEntry leafEntry = leafEntryService.findById(id);
        model.addAttribute("leafEntry", leafEntry);
        model.addAttribute("parties", partyService.getAllParties());
        model.addAttribute("todaysList", leafEntryService.getTodaysLeafEntries());
        model.addAttribute("todaysTotalWeight", leafEntryService.getTodaysTotalWeight());
        model.addAttribute("isEditMode", true);
        return "leaf-entry";
    }
   /* @GetMapping("/leaf-reports")
    public String getLeafReport(
            @RequestParam("entryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "branchCode", required = false) String branchCode,
            Model model) {

        List<LeafReportDTO> report = leafEntryService.getLeafReport(date, branchCode);

        double total = report.stream()
                .mapToDouble(r -> r.getQuantity().doubleValue())
                .sum();

        model.addAttribute("reports", report);
        model.addAttribute("total", total);
        model.addAttribute("date", date);
        model.addAttribute("branchCode", branchCode);

        return "leaf-reports";
    }
    @GetMapping("/leaf-reports")
    public String getLeafReportDefault(Model model){
        model.addAttribute("branches", branchRepository.findAll());
        return "leaf-reports";
    }*/
   @GetMapping("/leaf-reports")
    public String getLeafReport(
            @RequestParam(value = "entryDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "branchCode", required = false) String branchCode,
            Model model) {

        if (date != null) {
            branchCode = (branchCode != null && branchCode.isBlank()) ? null : branchCode;
            List<LeafReportDTO> report = leafEntryService.getLeafReport(date, branchCode);

            BigDecimal total = report.stream()
                    .map(LeafReportDTO::getQuantity)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("reports", report);
            model.addAttribute("total", total);
        }

        model.addAttribute("entryDate", date);
        model.addAttribute("branchCode", branchCode);
        model.addAttribute("branches", branchRepository.findAll());

        return "leaf-reports";
    }
}
