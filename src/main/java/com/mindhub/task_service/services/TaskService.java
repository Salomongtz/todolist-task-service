package com.mindhub.task_service.services;


import com.mindhub.task_service.models.TaskEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<TaskEntity> getTaskById(Long id);

    Flux<TaskEntity> getAllTasks();

    Mono<TaskEntity> createTask(TaskEntity task);

    Mono<TaskEntity> updateTask(Long id, TaskEntity task);

    Mono<Void> deleteTask(Long id);
}