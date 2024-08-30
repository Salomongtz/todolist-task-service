package com.mindhub.task_service.services.implementations;

import com.mindhub.task_service.TaskRepository;
import com.mindhub.task_service.models.TaskEntity;
import com.mindhub.task_service.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TaskServiceImplementation implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Mono<TaskEntity> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Flux<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Mono<TaskEntity> createTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    @Override
    public Mono<TaskEntity> updateTask(Long id, TaskEntity task) {
        return taskRepository.findById(id)
                .flatMap(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setStatus(task.getStatus());
                    existingTask.setUserId(task.getUserId()); // Update userId
                    return taskRepository.save(existingTask);
                });
    }

    @Override
    public Mono<Void> deleteTask(Long id) {
        return taskRepository.findById(id)
                .flatMap(taskRepository::delete);
    }
}
