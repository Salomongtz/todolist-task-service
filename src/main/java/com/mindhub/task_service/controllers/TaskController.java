package com.mindhub.task_service.controllers;

import com.mindhub.task_service.dtos.NewTaskRecord;
import com.mindhub.task_service.exceptions.TaskNotFoundException;
import com.mindhub.task_service.models.TaskEntity;
import com.mindhub.task_service.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Controller", description = "API for managing tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create a new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TaskEntity.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public Mono<ResponseEntity<TaskEntity>> createTask(@RequestBody NewTaskRecord task, ServerWebExchange exchange) {
        return taskService.createTask(task, exchange)
                .map(taskEntity -> ResponseEntity.created(URI.create("/tasks/" + taskEntity.getId())).body(taskEntity))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Get a task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TaskEntity.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> getTaskById(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @Operation(summary = "Get all tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TaskEntity.class)))
    })
    @GetMapping
    public Mono<ResponseEntity<Flux<TaskEntity>>> getAllTasks() {
        return Mono.just(ResponseEntity.ok(taskService.getAllTasks()));
    }

    @Operation(summary = "Update a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                            TaskEntity.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> updateTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id,
            @RequestBody NewTaskRecord task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new TaskNotFoundException(id)));
    }

    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Long id) {
        return taskService.deleteTask(id)
                .map(v -> ResponseEntity.ok().body("Task with ID " + id + " deleted successfully"))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with ID " + id + " not found")));
    }
}