package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 02-es modul belépési pontja.
 * Ugyanaz a struktúra mint a 01-es modulban – az összes @Component,
 * @Service, @Repository automatikusan felderítődik.
 */
@SpringBootApplication
public class IntegrationTestsApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationTestsApplication.class, args);
    }
}
