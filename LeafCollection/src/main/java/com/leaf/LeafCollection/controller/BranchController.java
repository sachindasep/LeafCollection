package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.service.BranchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public String listBranches(Model model) {
        model.addAttribute("branches", branchService.getAllBranches());
        model.addAttribute("branch", new Branch());
        return "branches";
    }

    @PostMapping("/save")
    public String saveBranch(@ModelAttribute Branch branch) {
        branchService.saveBranch(branch);
        return "redirect:/branches";
    }

    @GetMapping("/delete/{id}")
    public String deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return "redirect:/branches";
    }
}
