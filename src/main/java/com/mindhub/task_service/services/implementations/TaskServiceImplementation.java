package com.mindhub.task_service.services.implementations;

import com.mindhub.task_service.dtos.NewTaskRecord;
import com.mindhub.task_service.exceptions.NoTasksException;
import com.mindhub.task_service.exceptions.TaskNotFoundException;
import com.mindhub.task_service.repositories.TaskRepository;
import com.mindhub.task_service.models.TaskEntity;
import com.mindhub.task_service.services.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Mono<TaskEntity> getTaskById(Long id) {
        return Mono.just(id)
                .filter(Objects::nonNull)
                .filter(taskId -> taskId > 0)
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid id greater than 0")))
                .flatMap(taskRepository::findById)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }

    @Override
    public Flux<TaskEntity> getAllTasks() {
        return taskRepository.findAll()
                .switchIfEmpty(Flux.error(new NoTasksException()));
    }

    @Override
    public Mono<TaskEntity> createTask(NewTaskRecord newTask, ServerWebExchange exchange) {
        return Mono.just(newTask)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new NullPointerException("Task cannot be null")))
                .filter(task -> StringUtils.isNotBlank(task.title()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid title (title cannot be empty, must be less than 255 characters)")))
                .filter(task -> StringUtils.isNotBlank(task.description()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid description (description cannot be empty)")))
                .filter(task -> StringUtils.isNotBlank(task.status()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid status (status cannot be empty, must be less than 50 characters)")))
                .flatMap(newTaskRecord -> {
                    WebClient webClient = WebClient.create("http://localhost:8082");
                    return webClient.get()
                            .uri("/users/currentId")
                            .header("username", exchange.getRequest().getHeaders().getFirst("username"))
                            .retrieve()
                            .bodyToMono(Long.class)
                            .flatMap(userId -> taskRepository.save(new TaskEntity(newTaskRecord.title(), newTaskRecord.description(), newTaskRecord.status(), userId)));
                });
    }


    @Override
    public Mono<TaskEntity> updateTask(Long id, NewTaskRecord task) {
        return Mono.just(task)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new NullPointerException("Task cannot be null")))
                .filter(t -> StringUtils.isNotBlank(t.title()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid title (title cannot be empty, must be less than 255 characters)")))
                .filter(t -> StringUtils.isNotBlank(t.description()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid description (description cannot be empty)")))
                .filter(t -> StringUtils.isNotBlank(t.status()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid status (status cannot be empty, must be less than 50 characters)")))
                .flatMap(updatedTask -> taskRepository.findById(id)
                        .flatMap(existingTask -> {
                            existingTask.setTitle(updatedTask.title());
                            existingTask.setDescription(updatedTask.description());
                            existingTask.setStatus(updatedTask.status());
                            return taskRepository.save(existingTask);
                        }));
    }

    @Override
    public Mono<Void> deleteTask(Long id) {
        return Mono.just(id)
                .filter(i -> i != null && i > 0)
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid id greater than 0")))
                .flatMap(taskRepository::findById)
                .flatMap(taskRepository::delete);
    }
}
