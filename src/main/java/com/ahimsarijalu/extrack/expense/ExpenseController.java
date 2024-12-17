package com.ahimsarijalu.extrack.expense;

import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@Slf4j
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpenses() {

        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "Expense fetched successfully", expenses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getExpenseById(@PathVariable String id) {
        ExpenseDTO expenseDTO = expenseService.getExpenseById(id);
        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "Expense fetched successfully", expenseDTO))
                ;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> createExpense(@Valid @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO responseDTO = expenseService.saveExpense(expenseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApiResponse(HttpStatus.CREATED.value(), true, "Expense saved successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> updateExpense(@PathVariable String id, @RequestBody ExpenseDTO expenseDTO) {
        expenseService.updateExpense(id, expenseDTO);
        ExpenseDTO updatedExpense = expenseService.getExpenseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(HttpStatus.OK.value(), true, "Expense updated successfully", updatedExpense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(HttpStatus.OK.value(), true, "Expense deleted successfully", null));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> getAllExpensesByCategory(@PathVariable String category) {
        List<ExpenseDTO> expenses = expenseService.getAllExpensesByCategory(Category.valueOf(category.toUpperCase()));
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(
                HttpStatus.OK.value(),
                true,
                "Expenses by category of " + category.toUpperCase() + " fetched successfully",
                expenses
        ));
    }

    @GetMapping("/category/user/{userId}")
    public ResponseEntity<ApiResponse<TopCategoryDTO>> getTopCategoryByUserId(@PathVariable String userId) {
        TopCategoryDTO expenses = expenseService.findTopCategoryByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(
                HttpStatus.OK.value(),
                true,
                "Expenses by category of " + userId + " fetched successfully",
                expenses
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> searchByTitleAndDescription(@RequestParam String query) {
        List<ExpenseDTO> expenses = expenseService.searchByTitleAndDescription(query);
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(
                HttpStatus.OK.value(),
                true,
                "Expenses searched successfully",
                expenses
        ));
    }
}
