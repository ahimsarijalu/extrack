package com.ahimsarijalu.extrack.expense;

import com.ahimsarijalu.extrack.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpenses() {
        ApiResponse<List<ExpenseDTO>> response;
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            if (!expenses.isEmpty()) {
                response = mapToApiResponse(HttpStatus.OK.value(), true, "Expense fetched successfully", expenses);
            } else {
                response = mapToApiResponse(HttpStatus.NOT_FOUND.value(), false, "Expense not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch expense", null);
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpenseById(@PathVariable String id) {
        ApiResponse<ExpenseDTO> response;
        try {
            ExpenseDTO expenseDTO = expenseService.getExpenseById(id);
            if (expenseDTO != null) {
                response = mapToApiResponse(HttpStatus.OK.value(), true, "Expense fetched successfully", expenseDTO);
            } else {
                response = mapToApiResponse(HttpStatus.NOT_FOUND.value(), false, "Expense not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch expense, Reason: " + e.getMessage(), null);
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        ApiResponse<ExpenseDTO> response;
        try {
            ExpenseDTO responseDTO = expenseService.saveExpense(expenseDTO);
            response = mapToApiResponse(HttpStatus.CREATED.value(), true, "Expense saved successfully", responseDTO);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to save expense, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable String id, @RequestBody ExpenseDTO expenseDTO) {
        ApiResponse<ExpenseDTO> response;
        try {
            expenseService.updateExpense(id, expenseDTO);
            ExpenseDTO updatedExpense = expenseService.getExpenseById(id);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Expense updated successfully", updatedExpense);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to update expense, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        ApiResponse<Void> response;
        try {
            expenseService.deleteExpense(id);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Expense deleted successfully", null);
        } catch (Exception e) {
            if (e instanceof ExpenseNotFoundException) {
                response = mapToApiResponse(HttpStatus.NOT_FOUND.value(), false, "Failed to delete expense, Reason: " + e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to delete expense, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpensesByCategory(@PathVariable String category) {
        ApiResponse<List<ExpenseDTO>> response;
        try {
            List<ExpenseDTO> expenses = expenseService.getAllExpensesByCategory(Category.valueOf(category.toUpperCase()));
            response = mapToApiResponse(
                    HttpStatus.OK.value(),
                    true,
                    "Expenses by category of " + category.toUpperCase() + " fetched successfully",
                    expenses
            );

        } catch (Exception e) {
            response = mapToApiResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    false,
                    "Failed to fetch expenses by category of " + category.toUpperCase() + ", Reason: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> searchByTitleAndDescription(@RequestParam String query) {
        ApiResponse<List<ExpenseDTO>> response;
        try {
            List<ExpenseDTO> expenses = expenseService.searchByTitleAndDescription(query);
            response = mapToApiResponse(
                    HttpStatus.OK.value(),
                    true,
                    "Expenses searched successfully",
                    expenses
            );
        } catch (Exception e) {
            response = mapToApiResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    false,
                    "Failed to search expenses, Reason: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
