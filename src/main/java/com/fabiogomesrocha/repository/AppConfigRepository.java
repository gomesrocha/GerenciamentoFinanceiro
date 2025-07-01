package com.fabiogomesrocha.repository;

import com.fabiogomesrocha.model.AppConfig;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppConfigRepository implements PanacheRepository<AppConfig> {
    public String findValueByKey(String key) {
        AppConfig config = find("chave", key).firstResult();
        return config != null ? config.getValor() : null;
    }
}
