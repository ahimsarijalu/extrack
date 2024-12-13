package com.ahimsarijalu.extrack.user;

import com.ahimsarijalu.extrack.expense.ExpenseDTO;
import com.ahimsarijalu.extrack.expense.ExpenseService;
import com.ahimsarijalu.extrack.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
//    private final ExpenseService expenseService;

    public UserController(UserService userService) {
        this.userService = userService;
//        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        ApiResponse<List<UserDTO>> response;
        try {
            List<UserDTO> users = userService.getAllUsers();
//            users.forEach(user -> user.setExpenses(expenseService.getAllExpenseByUserId(user.getId())));
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Users fetched successfully", users);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch users", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {
        ApiResponse<UserDTO> response;
        try {
            UserDTO user = userService.getUserById(id);
//            user.setExpenses(expenseService.getAllExpenseByUserId(id));
            response = mapToApiResponse(HttpStatus.OK.value(), true, "Users fetched successfully", user);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to fetch users", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response;
        try {
            userService.updateUser(id, userDTO);
            UserDTO updatedUser = userService.getUserById(id);
//            List<ExpenseDTO> expenses = expenseService.getAllExpenseByUserId(id);
//            updatedUser.setExpenses(expenses);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "User updated successfully", updatedUser);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to update user", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        ApiResponse<UserDTO> response;
        try {
            UserDTO createdUser = userService.saveUser(userDTO);
            response = mapToApiResponse(HttpStatus.CREATED.value(), true, "User created successfully", createdUser);
        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to create user", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        ApiResponse<Void> response;
        try {
            userService.deleteUser(id);
            response = mapToApiResponse(HttpStatus.OK.value(), true, "User deleted successfully", null);

        } catch (Exception e) {
            response = mapToApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, "Failed to delete user, Reason: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
