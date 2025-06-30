package com.fabiogomesrocha.service;

import com.fabiogomesrocha.model.BankAccount;
import com.fabiogomesrocha.model.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@ApplicationScoped
public class TransactionService {

    public List<Transaction> listByBank(Long bankId) {
        return Transaction.list("bankAccount.id", bankId);
    }

    @Transactional
    public void save(Transaction transaction) {
        BankAccount bank = BankAccount.findById(transaction.bankAccount.id);

        if (transaction == null || transaction.bankAccount == null) {
            throw new IllegalArgumentException("Transaction and bank cannot be null");
        }
        if (transaction.type == Transaction.Type.CREDIT) {
            bank.balance = bank.balance.add(transaction.value);
        } else if (transaction.type == Transaction.Type.DEBIT) {
            bank.balance = bank.balance.subtract(transaction.value);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }
        transaction.persist();
        bank.persist();
    }

    public BigDecimal creditMonth() {
        LocalDate start = YearMonth.now().atDay(1);
        LocalDate end = YearMonth.now().atEndOfMonth();
        return Transaction.<Transaction>list("type = ?1 and date >= ?2 and date <= ?3",
                Transaction.Type.CREDIT,
                YearMonth.now().atDay(1),
                YearMonth.now().atEndOfMonth())
                .stream()
                .map(t -> t.value != null ? t.value : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal debitMonth() {
        LocalDate start = YearMonth.now().atDay(1);
        LocalDate end = YearMonth.now().atEndOfMonth();
        return Transaction.<Transaction>list("type = ?1 and date >= ?2 and date <= ?3",
                Transaction.Type.DEBIT,
                YearMonth.now().atDay(1),
                YearMonth.now().atEndOfMonth())
                .stream()
                .map(t -> t.value != null ? t.value : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}


