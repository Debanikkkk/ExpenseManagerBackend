package com.example.demo.dto.task;

import com.example.demo.entity.task.TaskPriority;
import com.example.demo.entity.task.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateRequest(
        @NotNull Long workspaceId,
        @NotBlank String title,
        @NotNull TaskStatus status,
        @NotNull TaskPriority priority,
        @NotBlank String due
) {
}
