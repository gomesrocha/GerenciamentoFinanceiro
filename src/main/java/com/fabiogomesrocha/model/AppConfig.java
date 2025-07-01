package com.fabiogomesrocha.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_config")
public class AppConfig {

    @Id
    @Column(name = "chave", nullable = false)
    private String chave;

    @Column(name = "valor", nullable = false)
    private String valor;

    // Getters e setters
    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
