package com.mindhub.task_service.exceptions;

public class NoTasksException extends RuntimeException {
    public NoTasksException() {
        super("There are no tasks for this user.");
    }
}
