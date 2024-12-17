package com.ahimsarijalu.extrack.repository;

import com.ahimsarijalu.extrack.user.User;
import com.ahimsarijalu.extrack.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryTest testInstance;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave_ReturnSavedUser() {
        // Arrange
        User user = getUser();
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User savedUser = userRepository.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testFindById_ReturnUser() {
        // Arrange
        User user = getUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userRepository.findById(user.getId());

        // Assert
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getId()).isEqualTo(user.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    public void testFindAll_ReturnUserList() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("John Doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setAge(30);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");
        user2.setPassword("password123");
        user2.setAge(25);

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userRepository.findAll();

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).contains(user1, user2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteById_VerifyMethodCalled() {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userRepository.deleteById(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testExistsById_ReturnTrue() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean exists = userRepository.existsById(userId);

        // Assert
        Assertions.assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    public void testCount_ReturnNumberOfUsers() {
        // Arrange
        long userCount = 3L;
        when(userRepository.count()).thenReturn(userCount);

        // Act
        long count = userRepository.count();

        // Assert
        Assertions.assertThat(count).isEqualTo(userCount);
        verify(userRepository, times(1)).count();
    }

    @Test
    public void testFindById_UserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userRepository.findById(userId);

        // Assert
        Assertions.assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteById_NonExistentUser() {
        // Arrange
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userRepository.deleteById(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testExistsById_UserNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean exists = userRepository.existsById(userId);

        // Assert
        Assertions.assertThat(exists).isFalse();
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    public void testFindAll_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        var result = userRepository.findAll();

        // Assert
        Assertions.assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testSave_NullInput() {
        // Arrange
        when(userRepository.save(null)).thenThrow(new IllegalArgumentException("Entity cannot be null"));

        // Act & Assert
        Assertions.assertThatThrownBy(() -> userRepository.save(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity cannot be null");
        verify(userRepository, times(1)).save(null);
    }




    private User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setAge(28);
        return user;
    }
}
