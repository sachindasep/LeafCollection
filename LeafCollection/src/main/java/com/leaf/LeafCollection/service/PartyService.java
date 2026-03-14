package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Party createParty(Party party, String branchCode) {
        Branch branch = (Branch) branchRepository.findByBranchCode(branchCode).orElseThrow();
        party.setPartyCode(generatePartyCode(branchCode));
        party.setBranch(branch);
        party.setCreatedAt(LocalDateTime.now());
        return partyRepository.save(party);
    }

    public String generatePartyCode(String branchCode) {
        // Find the party with the highest code for this branch
        Optional<Party> lastParty = partyRepository.findTopByPartyCodeStartingWithOrderByIdDesc(branchCode);
        int next = 1;
        if (lastParty.isPresent()) {
            String lastCode = lastParty.get().getPartyCode();
            String numberPart = lastCode.substring(branchCode.length());
            try {
                next = Integer.parseInt(numberPart) + 1;
            } catch (NumberFormatException e) {
                next = 1; // fallback if code is malformed
            }
        }

        return branchCode + String.format("%03d", next);
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