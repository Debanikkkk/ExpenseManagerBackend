package com.example.demo.dto.workspace;

import jakarta.validation.constraints.NotBlank;
public record WorkspaceCreateRequest(
        @NotBlank String icon,
        @NotBlank String iconBg,
        @NotBlank String title,
        @NotBlank String description
) {
}

