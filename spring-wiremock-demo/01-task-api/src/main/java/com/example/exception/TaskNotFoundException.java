package com.example.exception;

/**
 * Nem talált Task esetén dobott unchecked kivétel.
 * A GlobalExceptionHandler 404-es HTTP státusszá alakítja.
 * RuntimeException leszármazott – nem kell checked exception deklaráció a service-ben.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Task not found with id: " + id);
    }
}
