package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private BranchRepository branchRepository;

    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    public Party getParty(Long id) {
        return partyRepository.findById(id).orElseThrow();
    }

    public Party createParty(Party party, Long branchId) {

        Branch branch = branchRepository.findById(branchId).orElseThrow();

        party.setBranch(branch);
        party.setCreatedAt(LocalDateTime.now());

        return partyRepository.save(party);
    }

    public Party updateParty(Long id, Party updatedParty) {

        Party party = partyRepository.findById(id).orElseThrow();

        party.setName(updatedParty.getName());
        party.setPhone(updatedParty.getPhone());
        party.setAddress(updatedParty.getAddress());

        return partyRepository.save(party);
    }

    public void deleteParty(Long id) {
        partyRepository.deleteById(id);
    }
}