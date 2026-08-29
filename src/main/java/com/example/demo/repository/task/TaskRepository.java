package com.example.demo.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.task.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
