**Task Service API**
======================

**Overview**
------------

This is a RESTful API for managing tasks. It provides endpoints for creating, reading, updating, and deleting tasks.

**Endpoints**
------------

### Create Task

* **URL:** `/tasks`
* **Method:** `POST`
* **Request Body:** `TaskEntity`
* **Response:** `Mono<TaskEntity>`

### Get Task by ID

* **URL:** `/tasks/{id}`
* **Method:** `GET`
* **Response:** `Mono<TaskEntity>`

### Get All Tasks

* **URL:** `/tasks`
* **Method:** `GET`
* **Response:** `Flux<TaskEntity>`

### Update Task

* **URL:** `/tasks/{id}`
* **Method:** `PUT`
* **Request Body:** `TaskEntity`
* **Response:** `Mono<TaskEntity>`

### Delete Task

* **URL:** `/tasks/{id}`
* **Method:** `DELETE`
* **Response:** `Mono<Void>`

**Error Handling**
-----------------

* If a task is not found, a `TaskNotFoundException` is thrown.

**Dependencies**
---------------

* `com.mindhub.task_service.exceptions.TaskNotFoundException`
* `com.mindhub.task_service.models.TaskEntity`
* `com.mindhub.task_service.services.TaskService`
* `org.springframework.beans.factory.annotation.Autowired`
* `org.springframework.web.bind.annotation.RestController`
* `reactor.core.publisher.Flux`
* `reactor.core.publisher.Mono`

**Notes**
-------

* This API uses Spring WebFlux and Reactor for reactive programming.
* The `TaskService` interface is not included in this README, but it provides the business logic for task management.