package com.example.demo.repository.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.expense.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}