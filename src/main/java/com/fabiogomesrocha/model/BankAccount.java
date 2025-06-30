package com.fabiogomesrocha.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class BankAccount extends PanacheEntity {

    @Column(nullable = false)
    public String bank;
    @Column(nullable = false)
    public String agency;
    public String account;
    public String type;


    @Column(precision = 15, scale = 2)
    public BigDecimal balance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Transaction> transactions;

}

