package com.ahimsarijalu.extrack.expense;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;


public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findAllByCategory(Category category);

    List<Expense> findAllByUserId(UUID userId);

    List<Expense> findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    @Transactional
    @Query(name = "Expense.findTopCategoryByUserId", nativeQuery = true)
    TopCategoryDTO findTopCategoryByUserId(@Param("userId") UUID userId);

}
