package com.mindhub.task_service.dtos;


import lombok.Getter;

@Getter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long userId; // User ID field
}
