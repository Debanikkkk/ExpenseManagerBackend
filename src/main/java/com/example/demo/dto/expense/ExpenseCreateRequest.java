package com.example.demo.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExpenseCreateRequest(
        @NotBlank String title,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotNull LocalDate date
) {
}
