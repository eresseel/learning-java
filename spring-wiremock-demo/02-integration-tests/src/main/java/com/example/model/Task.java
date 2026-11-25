package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

/**
 * Task JPA entitás – az alkalmazás fő domain objektuma.
 *
 * Mezők:
 *  - id:          auto-generált PK (SEQUENCE strategy PostgreSQL-hez)
 *  - title:       kötelező, max 255 karakter
 *  - description: opcionális, max 1000 karakter
 *  - status:      enum – TODO / IN_PROGRESS / DONE
 *  - createdAt:   létrehozás időpontja, nem módosítható (@Column updatable=false)
 */
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "tasks_id_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // --- lifecycle callback ---

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    // --- constructors ---

    protected Task() { /* JPA */ }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // --- getters & setters ---

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
}
