package com.ahimsarijalu.extrack.controller;

import com.ahimsarijalu.extrack.expense.*;
import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExpenses_200() {
        List<ExpenseDTO> expenses = Collections.singletonList(new ExpenseDTO());
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        ResponseEntity<ApiResponse<List<ExpenseDTO>>> response = expenseController.getAllExpenses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expense fetched successfully", response.getBody().getMessage());
        assertEquals(expenses, response.getBody().getData());

        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void testGetAllExpenses_500() {
        when(expenseService.getAllExpenses()).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.getAllExpenses();
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).getAllExpenses();
    }

    @Test
    void testGetExpenseById_200() {
        String expenseId = "123";
        ExpenseDTO expenseDTO = new ExpenseDTO();
        when(expenseService.getExpenseById(expenseId)).thenReturn(expenseDTO);

        ResponseEntity<ApiResponse<ExpenseDTO>> response = expenseController.getExpenseById(expenseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expense fetched successfully", response.getBody().getMessage());
        assertEquals(expenseDTO, response.getBody().getData());

        verify(expenseService, times(1)).getExpenseById(expenseId);
    }

    @Test
    void testGetExpenseById_404() {
        String expenseId = "123";
        when(expenseService.getExpenseById(expenseId)).thenThrow(new EntityNotFoundException("Expense not found"));

       EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseController.getExpenseById(expenseId);
        });

        assertEquals("Expense not found", exception.getMessage());
        verify(expenseService, times(1)).getExpenseById(expenseId);
    }

    @Test
    void testGetExpenseById_500() {
        String expenseId = "123";
        when(expenseService.getExpenseById(expenseId)).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.getExpenseById(expenseId);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).getExpenseById(expenseId);
    }

    @Test
    void testCreateExpense_201() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        ExpenseDTO savedExpenseDTO = new ExpenseDTO();
        when(expenseService.saveExpense(expenseDTO)).thenReturn(savedExpenseDTO);

        ResponseEntity<ApiResponse<ExpenseDTO>> response = expenseController.createExpense(expenseDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expense saved successfully", response.getBody().getMessage());
        assertEquals(savedExpenseDTO, response.getBody().getData());

        verify(expenseService, times(1)).saveExpense(expenseDTO);
    }

    @Test
    void testCreateExpense_500() {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        when(expenseService.saveExpense(expenseDTO)).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.createExpense(expenseDTO);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).saveExpense(expenseDTO);
    }

    @Test
    void testUpdateExpense_200() {
        String expenseId = "123";
        ExpenseDTO expenseDTO = new ExpenseDTO();
        ExpenseDTO updatedExpenseDTO = new ExpenseDTO();
        when(expenseService.getExpenseById(expenseId)).thenReturn(updatedExpenseDTO);

        ResponseEntity<ApiResponse<ExpenseDTO>> response = expenseController.updateExpense(expenseId, expenseDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expense updated successfully", response.getBody().getMessage());
        assertEquals(updatedExpenseDTO, response.getBody().getData());

        verify(expenseService, times(1)).updateExpense(expenseId, expenseDTO);
        verify(expenseService, times(1)).getExpenseById(expenseId);
    }

    @Test
    void testUpdateExpense_404() {
        String expenseId = "123";
        ExpenseDTO expenseDTO = new ExpenseDTO();
        doThrow(new EntityNotFoundException("Expense not found"))
                .when(expenseService).updateExpense(expenseId, expenseDTO);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseController.updateExpense(expenseId, expenseDTO);
        });

        assertEquals("Expense not found", exception.getMessage());
        verify(expenseService, times(1)).updateExpense(expenseId, expenseDTO);
    }

    @Test
    void testUpdateExpense_500() {
        String expenseId = "123";
        ExpenseDTO expenseDTO = new ExpenseDTO();
        doThrow(new RuntimeException("Internal server error"))
                .when(expenseService).updateExpense(expenseId, expenseDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.updateExpense(expenseId, expenseDTO);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).updateExpense(expenseId, expenseDTO);
    }

    @Test
    void testDeleteExpense_200() {
        String expenseId = "123";
        doNothing().when(expenseService).deleteExpense(expenseId);

        ResponseEntity<ApiResponse<Void>> response = expenseController.deleteExpense(expenseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expense deleted successfully", response.getBody().getMessage());

        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testDeleteExpense_404() {
        String expenseId = "123";
        doThrow(new EntityNotFoundException("Expense not found"))
                .when(expenseService).deleteExpense(expenseId);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            expenseController.deleteExpense(expenseId);
        });

        assertEquals("Expense not found", exception.getMessage());
        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testDeleteExpense_500() {
        String expenseId = "123";
        doThrow(new RuntimeException("Internal server error"))
                .when(expenseService).deleteExpense(expenseId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.deleteExpense(expenseId);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).deleteExpense(expenseId);
    }

    @Test
    void testGetAllExpensesByCategory_200() {
        String category = "FOOD";
        List<ExpenseDTO> expenses = Collections.singletonList(new ExpenseDTO());
        when(expenseService.getAllExpensesByCategory(Category.valueOf(category))).thenReturn(expenses);

        ResponseEntity<ApiResponse<List<ExpenseDTO>>> response = expenseController.getAllExpensesByCategory(category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expenses by category of FOOD fetched successfully", response.getBody().getMessage());
        assertEquals(expenses, response.getBody().getData());

        verify(expenseService, times(1)).getAllExpensesByCategory(Category.valueOf(category));
    }

    @Test
    void testGetAllExpensesByCategory_500() {
        String category = "FOOD";
        when(expenseService.getAllExpensesByCategory(Category.valueOf(category))).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.getAllExpensesByCategory(category);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).getAllExpensesByCategory(Category.valueOf(category));
    }

    @Test
    void testGetTopCategoryByUserId_200() {
        String userId = "user123";
        TopCategoryDTO topCategoryDTO = new TopCategoryDTO();
        when(expenseService.findTopCategoryByUserId(userId)).thenReturn(topCategoryDTO);

        ResponseEntity<ApiResponse<TopCategoryDTO>> response = expenseController.getTopCategoryByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expenses by category of user123 fetched successfully", response.getBody().getMessage());
        assertEquals(topCategoryDTO, response.getBody().getData());

        verify(expenseService, times(1)).findTopCategoryByUserId(userId);
    }

    @Test
    void testGetTopCategoryByUserId_500() {
        String userId = "user123";
        when(expenseService.findTopCategoryByUserId(userId)).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.getTopCategoryByUserId(userId);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).findTopCategoryByUserId(userId);
    }

    @Test
    void testSearchByTitleAndDescription_200() {
        String query = "sample";
        List<ExpenseDTO> expenses = Collections.singletonList(new ExpenseDTO());
        when(expenseService.searchByTitleAndDescription(query)).thenReturn(expenses);

        ResponseEntity<ApiResponse<List<ExpenseDTO>>> response = expenseController.searchByTitleAndDescription(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getSuccess());
        assertEquals("Expenses searched successfully", response.getBody().getMessage());
        assertEquals(expenses, response.getBody().getData());

        verify(expenseService, times(1)).searchByTitleAndDescription(query);
    }

    @Test
    void testSearchByTitleAndDescription_500() {
        String query = "sample";
        when(expenseService.searchByTitleAndDescription(query)).thenThrow(new RuntimeException("Internal server error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            expenseController.searchByTitleAndDescription(query);
        });

        assertEquals("Internal server error", exception.getMessage());
        verify(expenseService, times(1)).searchByTitleAndDescription(query);
    }
}

