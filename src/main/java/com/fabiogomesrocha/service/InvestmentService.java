package com.fabiogomesrocha.service;

import com.fabiogomesrocha.model.Investiment;
import com.fabiogomesrocha.model.InvestmentValue;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvestmentService {

    public List<Investiment> listAll() {
        return Investiment.listAll();
    }

    public Optional<Investiment> findById(Long id) {
        return Investiment.findByIdOptional(id);
    }

    @Transactional
    public Investiment create(Investiment investiment) {
        investiment.persist();
        return investiment;
    }

    @Transactional
    public InvestmentValue addValue(Long investmentId, LocalDate date, BigDecimal value) {
        Investiment inv = Investiment.findById(investmentId);
        if (inv == null) {
            throw new IllegalArgumentException("Investment not found");
        }

        InvestmentValue val = new InvestmentValue();
        val.date = date;
        val.value = value;
        val.investment = inv;
        val.persist();

        inv.values.add(val);
        return val;
    }

    public List<InvestmentValue> getEvolution(Long investmentId) {
        Investiment inv = Investiment.findById(investmentId);
        if (inv == null) {
            throw new IllegalArgumentException("Investment not found");
        }

        inv.values.sort((v1, v2) -> v1.date.compareTo(v2.date));
        return inv.values;
    }
}
