package com.fabiogomesrocha;

import com.fabiogomesrocha.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class DataSeeder {

    @Transactional
    void onStart(@Observes StartupEvent ev) {
        System.out.println("DataSeeder executando...");
        if (User.count() == 0) {
            User admin = new User();
            admin.username = "admin";
            admin.password = "admin";
            admin.role = "admin";
            admin.persist();
            System.out.println("Usuário admin criado.");
        }
    }
}
