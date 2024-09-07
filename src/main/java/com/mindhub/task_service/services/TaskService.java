package com.mindhub.task_service.services;


import com.mindhub.task_service.dtos.NewTaskRecord;
import com.mindhub.task_service.models.TaskEntity;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Mono<TaskEntity> getTaskById(Long id);

    Flux<TaskEntity> getAllTasks();

    Mono<TaskEntity> createTask(NewTaskRecord task, ServerWebExchange serverWebExchange);

    Mono<TaskEntity> updateTask(Long id, NewTaskRecord task);

    Mono<Void> deleteTask(Long id);
}