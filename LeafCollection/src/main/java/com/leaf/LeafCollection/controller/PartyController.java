package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;

    @Autowired
    private BranchRepository branchRepository;

   /* @GetMapping
    public String listParties(Model model) {
        model.addAttribute("parties", partyService.getAllParties());
        return "party/list";
    }*/

    @GetMapping
    public String partyPage(Model model) {
        model.addAttribute("party", new Party());
        model.addAttribute("branches", branchRepository.findAll());
        return "parties";
    }

    @PostMapping("/save")
    public String saveParty(@ModelAttribute Party party,
                            @RequestParam Long branchId) {

        partyService.createParty(party, branchId);
        return "redirect:/parties";
    }

    @GetMapping("/edit/{id}")
    public String editParty(@PathVariable Long id, Model model) {

        model.addAttribute("party", partyService.getParty(id));
        model.addAttribute("branches", branchRepository.findAll());

        return "party/edit";
    }

    @PostMapping("/update/{id}")
    public String updateParty(@PathVariable Long id,
                              @ModelAttribute Party party) {

        partyService.updateParty(id, party);
        return "redirect:/parties";
    }

    @GetMapping("/delete/{id}")
    public String deleteParty(@PathVariable Long id) {

        partyService.deleteParty(id);
        return "redirect:/parties";
    }
}
