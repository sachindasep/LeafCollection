package com.leaf.LeafCollection.controller;

import com.leaf.LeafCollection.dto.LeafEntryDTO;
import com.leaf.LeafCollection.entity.LeafEntry;
import com.leaf.LeafCollection.service.LeafEntryService;
import com.leaf.LeafCollection.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
