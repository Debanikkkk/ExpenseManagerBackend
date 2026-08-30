package com.example.demo.dto.workspace;

import java.time.LocalDateTime;

public record WorkspaceDashboardResponse(
        Long id,
        String icon,
        String iconBg,
        String title,
        String description,
        Integer progress,
        // String updatedText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
