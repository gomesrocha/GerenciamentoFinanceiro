package com.fabiogomesrocha.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Investiment extends PanacheEntity {

    public String name;
    public String type;
    public LocalDate startDate;

    @OneToMany(mappedBy = "investment", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<InvestmentValue> values = new ArrayList<>();


}
