package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot belépési pont.
 * Önmagában nem tartalmaz teszteket – futtatáshoz valós PostgreSQL szükséges.
 * Lásd: 02-integration-tests és 03-wiremock-tests modulok a tesztelt verziókhoz.
 */
@SpringBootApplication
public class TaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
    }
}
