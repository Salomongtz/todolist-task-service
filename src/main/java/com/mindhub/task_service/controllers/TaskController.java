package com.mindhub.task_service.controllers;

import com.mindhub.task_service.exceptions.TaskNotFoundException;
import com.mindhub.task_service.models.TaskEntity;
import com.mindhub.task_service.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public Mono<TaskEntity> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public Flux<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public Mono<TaskEntity> createTask(@RequestBody TaskEntity task) {
        return taskService.createTask(task);
    }


    @PutMapping("/{id}")
    public Mono<TaskEntity> updateTask(@PathVariable Long id, @RequestBody TaskEntity task) {
        return taskService.updateTask(id, task)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }
}


