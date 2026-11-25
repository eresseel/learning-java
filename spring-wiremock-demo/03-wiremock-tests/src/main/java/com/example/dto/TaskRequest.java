package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Bejövő kérés DTO – Task létrehozáshoz / módosításhoz.
 * Validáció itt történik (@Valid a controllerben), nem az entitásban.
 * Record típus: Java 16+, immutable, boilerplate-mentes.
 */
public record TaskRequest(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description
) {}
