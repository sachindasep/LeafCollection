package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.dto.LeafEntryDTO;
import com.leaf.LeafCollection.dto.LeafReportDTO;
import com.leaf.LeafCollection.entity.LeafEntry;
import com.leaf.LeafCollection.repository.LeafEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeafEntryService {

    @Autowired
    private LeafEntryRepository leafEntryRepository;

    public LeafEntry save(LeafEntry leafEntry){
        return leafEntryRepository.save(leafEntry);
    }

    public List<LeafEntry> getEntriesByDate(LocalDate date){
        return leafEntryRepository.findByEntryDate(date);
    }

    public List<LeafEntryDTO> getTodaysLeafEntries(){
        return leafEntryRepository.getTodaysLeafEntries();
    }

    public BigDecimal getTodaysTotalWeight(){
        List<LeafEntryDTO> todaysEntries = getTodaysLeafEntries();
        return todaysEntries.stream()
                .map(LeafEntryDTO::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deleteById(Long id){
        leafEntryRepository.deleteById(id);
    }

    public LeafEntry findById(Long id){
        return leafEntryRepository.findById(id).orElseThrow(() -> new RuntimeException("LeafEntry not found"));
    }
    public List<LeafReportDTO> getLeafReport(LocalDate date, String branchCode) {
        return leafEntryRepository.getLeafReport(date, branchCode);
    }
}