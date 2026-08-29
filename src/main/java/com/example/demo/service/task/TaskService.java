package com.example.demo.service.task;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.task.TaskCreateRequest;
import com.example.demo.entity.task.Task;
import com.example.demo.entity.task.TaskPriority;
import com.example.demo.entity.task.TaskStatus;
import com.example.demo.repository.task.TaskRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    public Task create(TaskCreateRequest request) {
        Task task = new Task();
        task.setWorkspaceId(request.workspaceId());
        task.setTitle(request.title());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDue(request.due());
        return taskRepository.save(task);
    }

    public Optional<Task> update(Long id, TaskCreateRequest request) {
        return taskRepository.findById(id)
                .map(existing -> {
                    existing.setWorkspaceId(request.workspaceId());
                    existing.setTitle(request.title());
                    existing.setStatus(request.status());
                    existing.setPriority(request.priority());
                    existing.setDue(request.due());
                    return taskRepository.save(existing);
                });
    }

    public boolean delete(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }
}
