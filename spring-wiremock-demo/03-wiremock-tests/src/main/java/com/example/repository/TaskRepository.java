package com.example.repository;

import com.example.model.Task;
import com.example.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Task repository – Spring Data JPA.
 * JpaRepository<Task, Long> biztosítja az összes standard CRUD műveletet.
 * Egyedi lekérdezések: findByStatus – SQL generálódik a metódusnévből.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Szűrés státusz alapján.
     * Generált SQL: SELECT * FROM tasks WHERE status = ?
     */
    List<Task> findByStatus(TaskStatus status);
}
