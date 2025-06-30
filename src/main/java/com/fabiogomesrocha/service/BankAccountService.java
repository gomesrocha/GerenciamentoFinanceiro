package com.fabiogomesrocha.service;

import com.fabiogomesrocha.model.BankAccount;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class BankAccountService {

    /**
     * List all Bank Account.
     *
     * @return  bankAccount the bank account to save
     */
    public List<BankAccount> listAll() {
        return BankAccount.listAll();
    }

    @Transactional
    public BankAccount create(BankAccount bankAccount) {
        if (bankAccount == null) {
            throw new IllegalArgumentException("Bank account cannot be null");
        }
        bankAccount.persist();
        return bankAccount;
    }

    public BankAccount findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return BankAccount.findById(id);
    }
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        BankAccount bankAccount = BankAccount.findById(id);
        if (bankAccount != null) {
            bankAccount.delete();
        } else {
            throw new IllegalArgumentException("Bank account not found with ID: " + id);
        }
    }
    @Transactional
    public BankAccount update(BankAccount bankAccount) {
        if (bankAccount == null || bankAccount.id == null) {
            throw new IllegalArgumentException("Bank account and ID cannot be null");
        }
        BankAccount existingBankAccount = BankAccount.findById(bankAccount.id);
        if (existingBankAccount == null) {
            throw new IllegalArgumentException("Bank account not found with ID: " + bankAccount.id);
        }
        existingBankAccount.bank = bankAccount.bank;
        existingBankAccount.balance = bankAccount.balance;
        existingBankAccount.persist();
        return existingBankAccount;
    }

    public List<BankAccount> findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return BankAccount.list("name", name);
    }

    public List<BankAccount> findByBalanceRange(BigDecimal min, BigDecimal max) {
        return BankAccount.list("balance >= ?1 and balance <= ?2", min, max);
    }

    public List<BankAccount> findByType(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be null or empty");
        }
        return BankAccount.list("type", type);
    }
    public List<BankAccount> findByBank(String bank) {
        if (bank == null || bank.isEmpty()) {
            throw new IllegalArgumentException("Bank cannot be null or empty");
        }
        return BankAccount.list("bank", bank);
    }
    public List<BankAccount> findByAgency(String agency) {
        if (agency == null || agency.isEmpty()) {
            throw new IllegalArgumentException("Agency cannot be null or empty");
        }
        return BankAccount.list("agency", agency);
    }
    public List<BankAccount> findByAccount(String account) {
        if (account == null || account.isEmpty()) {
            throw new IllegalArgumentException("Account cannot be null or empty");
        }
        return BankAccount.list("account", account);
    }
    public List<BankAccount> findByBankAndType(String bank, String type) {
        if (bank == null || bank.isEmpty() || type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Bank and Type cannot be null or empty");
        }
        return BankAccount.list("bank = ?1 and type = ?2", bank, type);
    }
    public List<BankAccount> findByBankAndAgency(String bank, String agency) {
        if (bank == null || bank.isEmpty() || agency == null || agency.isEmpty()) {
            throw new IllegalArgumentException("Bank and Agency cannot be null or empty");
        }
        return BankAccount.list("bank = ?1 and agency = ?2", bank, agency);
    }
    public BigDecimal totalBalance() {
        return BankAccount.streamAll()
                .map(b -> ((BankAccount) b).balance != null ? ((BankAccount) b).balance : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
