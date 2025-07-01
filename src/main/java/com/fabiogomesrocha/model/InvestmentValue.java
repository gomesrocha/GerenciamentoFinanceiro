package com.fabiogomesrocha.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class InvestmentValue extends PanacheEntity {

    public LocalDate date;
    public BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "investment_id")
    public Investiment investment;
}
