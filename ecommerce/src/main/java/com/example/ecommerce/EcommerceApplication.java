package com.example.ecommerce;

import com.example.ecommerce.service.ProductIndexerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductIndexerService indexerService) {
        return args -> {
            indexerService.indexAllProducts();
        };
    }
}
