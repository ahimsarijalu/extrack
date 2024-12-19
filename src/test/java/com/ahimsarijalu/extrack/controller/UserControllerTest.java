package com.ahimsarijalu.extrack.controller;

import com.ahimsarijalu.extrack.user.UserController;
import com.ahimsarijalu.extrack.user.UserDTO;
import com.ahimsarijalu.extrack.user.UserService;
import com.ahimsarijalu.extrack.utils.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID().toString());
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setAge(30);
    }

    // GET /api/users - 200 OK
    @Test
    void testGetAllUsers_Success() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(List.of(userDTO));

        // Act
        ResponseEntity<ApiResponse<List<UserDTO>>> response = userController.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Users fetched successfully", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(1, response.getBody().getData().size());
        verify(userService, times(1)).getAllUsers();
    }

    // GET /api/users/{id} - 200 OK
    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userService.getUserById(userDTO.getId())).thenReturn(userDTO);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.getUserById(userDTO.getId());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Users fetched successfully", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(userDTO.getName(), response.getBody().getData().getName());
        verify(userService, times(1)).getUserById(userDTO.getId());
    }

    // GET /api/users/{id} - 404 Not Found
    @Test
    void testGetUserById_NotFound() {
        // Arrange
        String invalidId = UUID.randomUUID().toString();
        when(userService.getUserById(invalidId)).thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userController.getUserById(invalidId));

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).getUserById(invalidId);
    }

    // POST /api/users - 201 Created
    @Test
    void testCreateUser_Success() {
        // Arrange
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.createUser(userDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User created successfully", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(userDTO.getName(), response.getBody().getData().getName());
        verify(userService, times(1)).saveUser(userDTO);
    }

    // PUT /api/users/{id} - 200 OK
    @Test
    void testUpdateUser_Success() {
        // Arrange
        String userId = userDTO.getId();
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName("Updated Name");
        updatedUserDTO.setEmail(userDTO.getEmail());
        updatedUserDTO.setAge(userDTO.getAge());

        when(userService.getUserById(userId)).thenReturn(userDTO);
        doNothing().when(userService).updateUser(userId, updatedUserDTO);

        // Act
        ResponseEntity<ApiResponse<UserDTO>> response = userController.updateUser(userId, updatedUserDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User updated successfully", Objects.requireNonNull(response.getBody()).getMessage());
        verify(userService, times(1)).updateUser(userId, updatedUserDTO);
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        String invalidId = UUID.randomUUID().toString();
        doThrow(new EntityNotFoundException("User not found")).when(userService).updateUser(eq(invalidId), any(UserDTO.class));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userController.updateUser(invalidId, userDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).updateUser(eq(invalidId), any(UserDTO.class));
    }

    // DELETE /api/users/{id} - 200 OK
    @Test
    void testDeleteUser_Success() {
        // Arrange
        String userId = userDTO.getId();
        doNothing().when(userService).deleteUser(userId);

        // Act
        ResponseEntity<ApiResponse<Void>> response = userController.deleteUser(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", Objects.requireNonNull(response.getBody()).getMessage());
        verify(userService, times(1)).deleteUser(userId);
    }

    // DELETE /api/users/{id} - 404 Not Found
    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        String invalidId = UUID.randomUUID().toString();
        doThrow(new EntityNotFoundException("User not found")).when(userService).deleteUser(invalidId);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userController.deleteUser(invalidId));

        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).deleteUser(invalidId);
    }
}