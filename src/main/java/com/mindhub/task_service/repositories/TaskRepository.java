package com.mindhub.task_service.repositories;

import com.mindhub.task_service.models.TaskEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TaskRepository extends ReactiveCrudRepository<TaskEntity, Long> {
}
