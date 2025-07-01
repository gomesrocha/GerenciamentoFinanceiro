package com.fabiogomesrocha.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
public class Transaction extends PanacheEntity {

    public enum Type{
        CREDIT,
        DEBIT
    }

    @Enumerated(EnumType.STRING)
    public Type type;

    public String description;

    public LocalDate date;

    @Column(precision = 15, scale = 2)
    public BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "bankAccount_id")
    @JsonIgnore
    public BankAccount bankAccount;
}
