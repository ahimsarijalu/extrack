package com.ahimsarijalu.extrack.user;

import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ahimsarijalu.extrack.utils.MapperUtil.mapToApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "Users fetched successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "Users fetched successfully", user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
        UserDTO updatedUser = userService.getUserById(id);

        return ResponseEntity.ok(mapToApiResponse(HttpStatus.OK.value(), true, "User updated successfully", updatedUser));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToApiResponse(HttpStatus.CREATED.value(), true, "User created successfully", createdUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapToApiResponse(HttpStatus.OK.value(), true, "User deleted successfully", null));
    }

}
