package com.task.manager.service;

import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.task.manager.model.Task;
import com.task.manager.model.Task.Status;
import com.task.manager.repositories.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }


    @Transactional
    public Object updateStatus( Long id, Status updatedStatus) {
        return taskRepository.findById(id).map(task -> {
            var oldStatus = task.getStatus();
            task.setStatus(updatedStatus);
            task = taskRepository.save(task);
            if (updatedStatus == Status.DONE && oldStatus != Status.DONE) {
                tasksPublisher.notifyTaskDone(task);
            }
            return Map.entry("updated", true);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public record FilterParams(Optional<Status> status) {
    }

    public Page<Task> filter(FilterParams filterParams, Pageable pagination) {
        return taskRepository.findAll(TaskRepository.filteredBy( filterParams.status), pagination);
    }

    @Transactional
    public int updateTask(Map<String, String> updates, Long id) {
        return taskRepository.updateTask(id,
                updates.containsKey("subject") ? (String) updates.get("subject") : null,
                updates.containsKey("description") ? (String) updates.get("description") : null);

    }

    @Transactional
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Object create(Task task) {
        return null;
    }

}
