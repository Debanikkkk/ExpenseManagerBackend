package com.example.demo.service.expense;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.expense.ExpenseCreateRequest;
import com.example.demo.entity.expense.Expense;
import com.example.demo.repository.expense.ExpenseRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> getById(Long id) {
        return expenseRepository.findById(id);
    }

    public Expense create(ExpenseCreateRequest request) {
        Expense expense = new Expense();
        expense.setTitle(request.title());
        expense.setAmount(request.amount());
        expense.setDate(request.date());
        return expenseRepository.save(expense);
    }

    public Optional<Expense> update(Long id, ExpenseCreateRequest request) {
        return expenseRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(request.title());
                    existing.setAmount(request.amount());
                    existing.setDate(request.date());
                    return expenseRepository.save(existing);
                });
    }

    public boolean delete(Long id) {
        if (!expenseRepository.existsById(id)) {
            return false;
        }
        expenseRepository.deleteById(id);
        return true;
    }
}
