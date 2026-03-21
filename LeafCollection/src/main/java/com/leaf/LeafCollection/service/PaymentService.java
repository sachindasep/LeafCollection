package com.leaf.LeafCollection.service;

import com.leaf.LeafCollection.entity.Branch;
import com.leaf.LeafCollection.entity.Party;
import com.leaf.LeafCollection.entity.Payment;
import com.leaf.LeafCollection.repository.BranchRepository;
import com.leaf.LeafCollection.repository.PartyRepository;
import com.leaf.LeafCollection.repository.PaymentRepository;
import com.leaf.LeafCollection.utils.LedgerType;
import com.leaf.LeafCollection.utils.PaymentMode;
import com.leaf.LeafCollection.utils.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PartyRepository partyRepository;
    private final BranchRepository branchRepository;
    private final LedgerService ledgerService;

    public PaymentService(PaymentRepository paymentRepository,
                          PartyRepository partyRepository,
                          BranchRepository branchRepository,
                          LedgerService ledgerService) {
        this.paymentRepository = paymentRepository;
        this.partyRepository = partyRepository;
        this.branchRepository = branchRepository;
        this.ledgerService = ledgerService;
    }

    @Transactional
    public void savePayment(Long partyId,
                            LocalDate paymentDate,
                            BigDecimal amount,
                            String paymentMode,
                            String remarks) {

        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new RuntimeException("Party not found"));

      /*  Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
*/
        // 1. Save Payment
        Payment payment = new Payment();
        payment.setParty(party);
        payment.setPaymentDate(paymentDate);
        payment.setAmount(amount);
        payment.setRemarks(remarks);

        if (paymentMode != null) {
            payment.setPaymentMode(PaymentMode.valueOf(paymentMode));
        }

        payment = paymentRepository.save(payment);

        // 2. Create Ledger Entry (IMPORTANT)
        ledgerService.createEntry(
                party,
                paymentDate,
                LedgerType.ADVANCE,
                BigDecimal.ZERO,
                amount,
                payment.getId(),
                "Advance Payment"
        );
    }
    @Transactional
    public void updatePayment(Long paymentId,
                              BigDecimal newAmount,
                              String remarks) {

        Payment oldPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (oldPayment.getStatus() == PaymentStatus.REVERSED) {
            throw new RuntimeException("Already reversed");
        }

        Party party = oldPayment.getParty();
       // Branch branch = oldPayment.getBranch();

        // 1. Reverse old payment in ledger
        ledgerService.createEntry(
                party,
                LocalDate.now(),
                LedgerType.ADJUSTMENT,
                oldPayment.getAmount(),   // CREDIT (reverse)
                BigDecimal.ZERO,
                oldPayment.getId(),
                "Reversal of payment ID " + paymentId
        );

        // 2. Mark old payment as reversed
        oldPayment.setStatus(PaymentStatus.REVERSED);
        paymentRepository.save(oldPayment);

        // 3. Create new payment
        Payment newPayment = new Payment();
        newPayment.setParty(party);
       // newPayment.setBranch(branch);
        newPayment.setPaymentDate(oldPayment.getPaymentDate());
        newPayment.setAmount(newAmount);
        newPayment.setRemarks("Edited: " + remarks);

        newPayment = paymentRepository.save(newPayment);

        // 4. New ledger entry
        ledgerService.createEntry(
                party,
                newPayment.getPaymentDate(),
                LedgerType.ADVANCE,
                BigDecimal.ZERO,
                newAmount,
                newPayment.getId(),
                "Updated payment"
        );
    }
    public List<Payment> getTodayPayments() {
        return paymentRepository.findTodayPayments(LocalDate.now());
    }

    public BigDecimal getTodayTotal() {
        return paymentRepository.getTodayTotal(LocalDate.now());
    }
}
