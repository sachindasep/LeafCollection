package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.entity.LeafEntry;
import com.leaf.LeafCollection.service.LeafEntryService;
import com.leaf.LeafCollection.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/leaf-entry")
public class LeafEntryController {

    @Autowired
    private LeafEntryService leafEntryService;

    @Autowired
    private PartyService partyService;

    @GetMapping
    public String leafEntryPage(Model model){

        model.addAttribute("leafEntry", new LeafEntry());
        model.addAttribute("parties", partyService.getAllParties());

        return "leaf-entry";
    }

    @PostMapping("/save")
    public String saveLeafEntry(@ModelAttribute LeafEntry leafEntry){

        leafEntryService.save(leafEntry);

        return "redirect:/leaf-entry";
    }
}
