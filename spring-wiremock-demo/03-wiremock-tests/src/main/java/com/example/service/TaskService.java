package com.example.service;

import com.example.client.NotificationClient;
import com.example.dto.TaskRequest;
import com.example.dto.TaskResponse;
import com.example.exception.TaskNotFoundException;
import com.example.model.Task;
import com.example.model.TaskStatus;
import com.example.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Bővített TaskService – a 01/02 modulhoz képest integrálja a NotificationClient-et.
 *
 * Kulcs változás:
 *  create() metódus task mentés után meghívja sendTaskCreatedNotification()-t.
 *  Ha az értesítés sikertelen, a task MÉGIS létrejön (best-effort értesítés).
 *
 * Ez a "resilience" minta tesztelése a WireMock segítségével:
 *  - Mi történik ha az értesítési szolgáltatás 500-at ad vissza?
 *  - Mi történik ha timeout van?
 *  → A task mindig mentésre kerül, csak az értesítés maradhat el.
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final NotificationClient notificationClient;

    public TaskService(TaskRepository taskRepository, NotificationClient notificationClient) {
        this.taskRepository = taskRepository;
        this.notificationClient = notificationClient;
    }

    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::from)
                .toList();
    }

    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.from(task);
    }

    public List<TaskResponse> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status).stream()
                .map(TaskResponse::from)
                .toList();
    }

    /**
     * Task létrehozása + értesítés küldése.
     *
     * Tranzakció:
     *  1. Task mentés (ha ez sikertelen, kivétel → rollback, értesítés nem megy)
     *  2. Értesítés küldése (ha ez sikertelen, csak logolunk → task MEGMARAD)
     *
     * Kockázat: "at-least-once" értesítés kellene (Outbox pattern) ha garantált
     * kézbesítés kell – ez a példa csak best-effort.
     */
    @Transactional
    public TaskResponse create(TaskRequest request) {
        // 1. Adatbázis mentés
        Task task = new Task(request.title(), request.description());
        Task saved = taskRepository.save(task);

        // 2. Best-effort értesítés (hiba esetén csak logolás, nem dobunk kivételt)
        boolean notified = notificationClient.sendTaskCreatedNotification(
                saved.getId(), saved.getTitle());

        if (!notified) {
            log.warn("Task {} created but notification could not be sent", saved.getId());
        }

        return TaskResponse.from(saved);
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(request.title());
        task.setDescription(request.description());
        return TaskResponse.from(task);
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
