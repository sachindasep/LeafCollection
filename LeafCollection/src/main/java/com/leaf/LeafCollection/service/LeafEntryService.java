package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.LeafEntry;
import com.leaf.LeafCollection.repository.LeafEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}