package com.ahimsarijalu.extrack.expense;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findAllByCategory(Category category);
    List<Expense> findAllByUserId(UUID userId);
    List<Expense> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
