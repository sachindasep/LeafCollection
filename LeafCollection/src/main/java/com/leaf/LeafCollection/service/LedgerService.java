package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.entity.LedgerEntry;
import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.repository.LedgerRepository;
import com.leaf.LeafCollection.utils.LedgerType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LedgerService {

    private final LedgerRepository ledgerRepository;

    public LedgerService(LedgerRepository ledgerRepository) {
        this.ledgerRepository = ledgerRepository;
    }

    public LedgerEntry createEntry(Party party,
                                   LocalDate date,
                                   LedgerType type,
                                   BigDecimal credit,
                                   BigDecimal debit,
                                   Long referenceId,
                                   String remarks) {

        BigDecimal lastBalance = ledgerRepository
                .findTopByPartyIdOrderByEntryDateDescIdDesc(party.getId())
                .map(LedgerEntry::getBalance)
                .orElse(BigDecimal.ZERO);

        BigDecimal newBalance = lastBalance
                .add(credit != null ? credit : BigDecimal.ZERO)
                .subtract(debit != null ? debit : BigDecimal.ZERO);

        LedgerEntry entry = new LedgerEntry();
        entry.setParty(party);
        entry.setEntryDate(date);
        entry.setType(type);
        entry.setCredit(credit);
        entry.setDebit(debit);
        entry.setBalance(newBalance);
        entry.setReferenceId(referenceId);
        entry.setRemarks(remarks);

        return ledgerRepository.save(entry);
    }
    @Transactional
    public void recalculateLedger(Long partyId) {

        List<LedgerEntry> entries =
                ledgerRepository.findByPartyIdOrderByEntryDateAscIdAsc(partyId);

        BigDecimal runningBalance = BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {

            BigDecimal credit = entry.getCredit() != null ? entry.getCredit() : BigDecimal.ZERO;
            BigDecimal debit = entry.getDebit() != null ? entry.getDebit() : BigDecimal.ZERO;

            runningBalance = runningBalance.add(credit).subtract(debit);

            entry.setBalance(runningBalance);
        }

        ledgerRepository.saveAll(entries);
    }
}
