package com.mindhub.task_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;

@Table("tasks")
@Data
public class TaskEntity {
    @Id
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long userId; // Field for the user ID

    public TaskEntity(String title, String description, String status, Long userId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.userId = userId;
    }
}