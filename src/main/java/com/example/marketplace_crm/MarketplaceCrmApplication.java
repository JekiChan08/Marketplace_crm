package com.example.marketplace_crm;

import jakarta.persistence.Cacheable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class MarketplaceCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceCrmApplication.class, args);
    }

}
