package com.example.dto;

import com.example.model.Task;
import com.example.model.TaskStatus;

import java.time.Instant;

/**
 * Kimenő válasz DTO – Task entitásból épül fel.
 * Az entitást sosem adjuk vissza közvetlenül: a DTO leválasztja az API-t a belső modelltől.
 * Statikus factory: TaskResponse.from(task) – olvasható és tesztelhető.
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt
) {
    /**
     * Entitásból DTO konverzió.
     * Csak azokat a mezőket exportálja, amelyek a kliensnek kellenek.
     */
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}
