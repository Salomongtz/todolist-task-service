package com.mindhub.task_service.services.implementations;

import com.mindhub.task_service.exceptions.NoTasksException;
import com.mindhub.task_service.exceptions.TaskNotFoundException;
import com.mindhub.task_service.repositories.TaskRepository;
import com.mindhub.task_service.models.TaskEntity;
import com.mindhub.task_service.services.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
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
    public Mono<TaskEntity> createTask(TaskEntity newTask) {
        return Mono.just(newTask)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new NullPointerException("Task cannot be null")))
                .filter(task -> StringUtils.isNotBlank(task.getTitle()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid title (title cannot be empty, must be less than 255 characters)")))
                .filter(task -> StringUtils.isNotBlank(task.getDescription()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid description (description cannot be empty)")))
                .filter(task -> StringUtils.isNotBlank(task.getStatus()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid status (status cannot be empty, must be less than 50 characters)")))
                .filter(task -> task.getUserId() != null && task.getUserId() > 0)
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid user id greater than 0")))
                .flatMap(taskRepository::save);
    }


    @Override
    public Mono<TaskEntity> updateTask(Long id, TaskEntity task) {
        return Mono.just(task)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new NullPointerException("Task cannot be null")))
                .filter(t -> StringUtils.isNotBlank(t.getTitle()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid title (title cannot be empty, must be less than 255 characters)")))
                .filter(t -> StringUtils.isNotBlank(t.getDescription()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid description (description cannot be empty)")))
                .filter(t -> StringUtils.isNotBlank(t.getStatus()))
                .switchIfEmpty(Mono.error(new DataIntegrityViolationException("Must enter a valid status (status cannot be empty, must be less than 50 characters)")))
                .flatMap(updatedTask -> taskRepository.findById(id)
                        .flatMap(existingTask -> {
                            existingTask.setTitle(updatedTask.getTitle());
                            existingTask.setDescription(updatedTask.getDescription());
                            existingTask.setStatus(updatedTask.getStatus());
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
