package com.ahimsarijalu.extrack.service;

import com.ahimsarijalu.extrack.expense.*;
import com.ahimsarijalu.extrack.fund.Fund;
import com.ahimsarijalu.extrack.fund.FundRepository;
import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private ExpenseDTO expenseDTO;
    private Expense expense;
    private User user;
    private Fund fund;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());

        fund = new Fund();
        fund.setId(UUID.randomUUID());
        fund.setBalance(1000L);

        expense = new Expense();
        expense.setId(UUID.randomUUID());
        expense.setAmount(500L);
        expense.setUser(user);
        expense.setFund(fund);

        expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId().toString());
        expenseDTO.setAmount(500L);
        expenseDTO.setUserId(user.getId().toString());
        expenseDTO.setFundId(fund.getId().toString());
    }

    @Test
    void testGetAllExpenses_200() {
        when(expenseRepository.findAll()).thenReturn(Collections.singletonList(expense));

        assertDoesNotThrow(() -> {
            var expenses = expenseService.getAllExpenses();
            assertFalse(expenses.isEmpty());
        });
    }

    @Test
    void testGetAllExpenses_404() {
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.getAllExpenses();
        });

        assertEquals("No Expenses found", exception.getMessage());
    }

    @Test
    void testSaveExpense_201() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        ExpenseDTO savedExpense = expenseService.saveExpense(expenseDTO);

        assertNotNull(savedExpense);
        assertEquals(expenseDTO.getAmount(), savedExpense.getAmount());
    }

    @Test
    void testSaveExpense_500() {
        expenseDTO.setAmount(2000L); // Exceeds fund balance

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(fundRepository.findById(fund.getId())).thenReturn(Optional.of(fund));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            expenseService.saveExpense(expenseDTO);
        });

        assertEquals("Total amount cannot be negative", exception.getMessage());
    }

    @Test
    void testGetExpenseById_200() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));

        ExpenseDTO foundExpense = expenseService.getExpenseById(expense.getId().toString());

        assertNotNull(foundExpense);
        assertEquals(expense.getId().toString(), foundExpense.getId());
    }

    @Test
    void testGetExpenseById_404() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.getExpenseById(expense.getId().toString());
        });

        assertEquals("Expense with id " + expense.getId() + " not found", exception.getMessage());
    }

    @Test
    void testUpdateExpense_200() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        assertDoesNotThrow(() -> {
            expenseService.updateExpense(expense.getId().toString(), expenseDTO);
        });
    }

    @Test
    void testUpdateExpense_404() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.updateExpense(expense.getId().toString(), expenseDTO);
        });

        assertEquals("Expense with id " + expense.getId() + " not found", exception.getMessage());
    }

    @Test
    void testDeleteExpense_200() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.of(expense));

        assertDoesNotThrow(() -> {
            expenseService.deleteExpense(expense.getId().toString());
        });

        verify(expenseRepository, times(1)).delete(expense);
    }

    @Test
    void testDeleteExpense_404() {
        when(expenseRepository.findById(expense.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.deleteExpense(expense.getId().toString());
        });

        assertEquals("Expense with id " + expense.getId() + " not found", exception.getMessage());
    }

    @Test
    void testGetAllExpensesByCategory() {
        Category category = Category.FOOD;
        when(expenseRepository.findAllByCategory(category)).thenReturn(Collections.singletonList(expense));

        var expenses = expenseService.getAllExpensesByCategory(category);

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
    }

    @Test
    void testSearchByTitleAndDescription() {
        String query = "test";
        when(expenseRepository.findAllByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query))
                .thenReturn(Collections.singletonList(expense));

        var expenses = expenseService.searchByTitleAndDescription(query);

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
    }

    @Test
    void testGetAllExpenseByUserId() {
        when(expenseRepository.findAllByUserId(user.getId())).thenReturn(Collections.singletonList(expense));

        var expenses = expenseService.getAllExpenseByUserId(user.getId().toString());

        assertNotNull(expenses);
        assertFalse(expenses.isEmpty());
    }

    @Test
    void testFindTopCategoryByUserId_200() {
        TopCategoryDTO topCategoryDTO = new TopCategoryDTO();
        when(expenseRepository.findTopCategoryByUserId(user.getId())).thenReturn(topCategoryDTO);

        TopCategoryDTO result = expenseService.findTopCategoryByUserId(user.getId().toString());

        assertNotNull(result);
    }

    @Test
    void testFindTopCategoryByUserId_404() {
        when(expenseRepository.findTopCategoryByUserId(user.getId())).thenReturn(null);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseService.findTopCategoryByUserId(user.getId().toString());
        });

        assertEquals("Top category not found for user ID: " + user.getId(), exception.getMessage());
    }
}
