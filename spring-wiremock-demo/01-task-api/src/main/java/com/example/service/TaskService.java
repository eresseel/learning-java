package com.example.service;

import com.example.dto.TaskRequest;
import com.example.dto.TaskResponse;
import com.example.exception.TaskNotFoundException;
import com.example.model.Task;
import com.example.model.TaskStatus;
import com.example.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Task üzleti logika réteg.
 *
 * Felelősségek:
 *  - entitás ↔ DTO konverzió
 *  - tranzakció kezelés (@Transactional)
 *  - hibadobás (TaskNotFoundException)
 *
 * Tranzakciós stratégia:
 *  - readOnly = true az olvasó metódusoknál (flush skip + 2nd level cache hint)
 *  - írónál default (readOnly = false)
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /** Összes task visszaadása. */
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    /** Egy task keresése id alapján; 404 ha nincs. */
    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.from(task);
    }

    /** Státusz alapján szűrés. */
    public List<TaskResponse> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskResponse::from)
                .toList();
    }

    /** Új task létrehozása; 201-es kódot a controller ad. */
    @Transactional
    public TaskResponse create(TaskRequest request) {
        Task task = new Task(request.title(), request.description());
        Task saved = taskRepository.save(task);
        return TaskResponse.from(saved);
    }

    /** Task frissítése; 200 visszatérés az aktuális állapottal. */
    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(request.title());
        task.setDescription(request.description());
        // JPA dirty checking automatikusan menteni fogja a változást tranzakció végén
        return TaskResponse.from(task);
    }

    /** Task törlése; ha nem létezik, 404. */
    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
