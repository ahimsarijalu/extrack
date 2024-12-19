package com.ahimsarijalu.extrack.service;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserDTO;
import com.ahimsarijalu.extrack.user.UserRepository;
import com.ahimsarijalu.extrack.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");
        user.setAge(30);

        userDTO = new UserDTO();
        userDTO.setId(user.getId().toString());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
    }

    @Test
    void testGetAllUsers_Success() {
       //Arrange
        when(userRepository.findAll()).thenReturn(List.of(user));

        //Act
        List<UserDTO> result = userService.getAllUsers();

        //Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getName(), result.getFirst().getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_NoUsersFound() {
       //Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        //Act
        Exception exception = assertThrows(EntityNotFoundException.class, userService::getAllUsers);

        //Assert
        assertEquals("No users is found", exception.getMessage());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
       //Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        //Act
        UserDTO result = userService.getUserById(user.getId().toString());

        //Assert
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void testGetUserById_UserNotFound() {
       //Arrange
        UUID invalidId = UUID.randomUUID();
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        //Act
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.getUserById(invalidId.toString()));

        //Assert
        assertEquals("User with this id: " + invalidId + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(invalidId);
    }

    @Test
    void testSaveUser_Success() {
       //Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        //Act
        UserDTO result = userService.saveUser(userDTO);

        //Assert
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_InternalError() {
       //Arrange
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Unexpected error"));

        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> userService.saveUser(userDTO));

        //Assert
        assertEquals("Unexpected error", exception.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
       //Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        //Act
        userDTO.setName("Updated Name");
        userService.updateUser(user.getId().toString(), userDTO);

        //Assert
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("Updated Name", user.getName());
    }

    @Test
    void testUpdateUser_UserNotFound() {
       //Arrange
        UUID invalidId = UUID.randomUUID();
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        //Act
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUser(invalidId.toString(), userDTO));

        //Assert
        assertEquals("User with this id: " + invalidId + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(invalidId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_InternalError() {
       //Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Unexpected error"));

        //Act
        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(user.getId().toString(), userDTO));

        //Assert
        assertEquals("Unexpected error", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        //Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(user.getId());

        //Act
        userService.deleteUser(user.getId().toString());

        //Assert
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        //Arrange
        UUID invalidId = UUID.randomUUID();
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        //Act
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> userService.deleteUser(invalidId.toString()));

        //Assert
        assertEquals("User with this id: " + invalidId + " is not found", exception.getMessage());
        verify(userRepository, times(1)).findById(invalidId);
        verify(userRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testDeleteUser_InternalError() {
        //Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Unexpected error")).when(userRepository).deleteById(user.getId());

        //Act
        Exception exception = assertThrows(RuntimeException.class,
                () -> userService.deleteUser(user.getId().toString()));

        //Assert
        assertEquals("Unexpected error", exception.getMessage());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}