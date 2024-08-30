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
}