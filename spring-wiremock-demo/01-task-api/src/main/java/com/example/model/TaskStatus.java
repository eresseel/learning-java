package com.example.model;

/**
 * Task állapot enum.
 * Az adatbázisban String-ként tárolódik (@Enumerated(EnumType.STRING)),
 * így olvasható marad és sémaváltozás nélkül bővíthető.
 */
public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE
}
