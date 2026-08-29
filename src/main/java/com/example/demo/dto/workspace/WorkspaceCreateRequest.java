package com.example.demo.dto.workspace;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record WorkspaceCreateRequest(
        @NotBlank String icon,
        @NotBlank String iconBg,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull @Min(0) @Max(100) Integer progress,
        @NotBlank String updatedText
) {
}
