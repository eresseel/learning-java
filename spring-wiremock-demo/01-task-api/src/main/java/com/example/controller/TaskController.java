package com.example.controller;

import com.example.dto.TaskRequest;
import com.example.dto.TaskResponse;
import com.example.model.TaskStatus;
import com.example.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Task REST controller.
 *
 * Végpontok:
 *  POST   /api/tasks              → 201 Created + Location header
 *  GET    /api/tasks              → 200 OK (opcionális ?status= szűrő)
 *  GET    /api/tasks/{id}         → 200 OK | 404 Not Found
 *  PUT    /api/tasks/{id}         → 200 OK | 404 Not Found
 *  DELETE /api/tasks/{id}         → 204 No Content | 404 Not Found
 *
 * Konvenció:
 *  - @Valid aktiválja a bean validation-t a request body-n
 *  - ResponseEntity<Void> a 204-es törléshez (body nélkül)
 *  - Location header 201-nél: iparági szabvány, megkönnyíti a kliens integrációt
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Task létrehozása.
     * @return 201 Created + Location: /api/tasks/{id}
     */
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse created = taskService.create(request);

        // Location header az RFC 7231 szerint: az új erőforrás URI-ja
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Összes task lekérdezése; opcionálisan status alapján szűrve.
     * Pl.: GET /api/tasks?status=TODO
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(
            @RequestParam(required = false) TaskStatus status) {

        List<TaskResponse> tasks = (status != null)
                ? taskService.findByStatus(status)
                : taskService.findAll();

        return ResponseEntity.ok(tasks);
    }

    /**
     * Egy task lekérdezése id alapján.
     * @return 200 OK | 404 Not Found (GlobalExceptionHandler kezeli)
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    /**
     * Task teljes frissítése (PUT = idempotent replace szemantika).
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }

    /**
     * Task törlése.
     * @return 204 No Content (body nélkül) | 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
