package com.luxuryproductsholding.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.luxuryproductsholding.api.DAO")
public class BackendWebshop {

    public static void main(String[] args) {
        SpringApplication.run(BackendWebshop.class, args);
    }
}
