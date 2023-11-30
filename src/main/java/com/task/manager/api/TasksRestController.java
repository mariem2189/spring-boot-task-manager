package com.task.manager.api;

import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.manager.model.Task;
import com.task.manager.model.Task.Status;
import com.task.manager.service.TaskService;
import com.task.manager.service.TaskService.FilterParams;

@RestController
@RequestMapping("tasks")
public class TasksRestController {
    private final TaskService taskService;

    public TasksRestController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTask(@PathVariable Long id) {

        return ResponseEntity.of(taskService.findById(id));
    }

    @GetMapping("/all")
    public Iterable<Task> findAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Task>> filterByStatus(
            @RequestParam("filter.status") Optional<Status> status, Pageable pagination) {
        return ResponseEntity.ok(
                taskService.filter(new FilterParams(status), pagination));
    }

    @PostMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.create(task));
    }

    public record UpdateStatusRequest(Status status) {
    }

    @PostMapping(path = "/{id}/status", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTask(@PathVariable Long id,
            @RequestBody UpdateStatusRequest statusReq) {
        return ResponseEntity.ok(taskService.updateStatus(id, statusReq.status));
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> partialUpdateGeneric(@RequestBody Map<String, String> updates,
            @PathVariable("id") Long id) {
        taskService.updateTask(updates, id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}

