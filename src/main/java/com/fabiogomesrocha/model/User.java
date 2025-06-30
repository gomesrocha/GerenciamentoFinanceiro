package com.fabiogomesrocha.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {

    @NotNull
    @Column(unique = true)
    public String username;

    @NotNull
    public String password;

    @NotNull
    public String role;
}
