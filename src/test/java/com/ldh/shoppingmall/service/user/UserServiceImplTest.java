package com.ldh.shoppingmall.service.user;

import com.ldh.shoppingmall.dto.user.UserDto;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.ldh.shoppingmall.entity.user.User.Role;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // Mock Repository

    @Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder

    @InjectMocks
    private UserServiceImpl userServiceImpl; // Object that is tested

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this); // initialize Mock object
    }

    @Test
    @DisplayName("Register Success")
    void register_ShouldSaveUser_WhenUsernameIsUnique() {

        // Given
        UserDto userDto = new UserDto("newUser", "password");
        User mockSaveduser = new User(userDto.getUsername(), "encodedPassword", Role.USER);

        // No exists username
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        // Encoded password Mock
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(mockSaveduser);

        // when
        userServiceImpl.register(userDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Register Fails When Username Already Exists")
    void register_ShouldNotSaveUser_WhenUsernameIsUnique() {

        // Given
        UserDto userDto = new UserDto("existingUser", "password");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // When and Then
        assertThatThrownBy(() -> userServiceImpl.register(userDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("409 CONFLICT \"Username already exists.\"");


        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Remove User Success")
    void removeUser_ShouldDelete_WhenUserExists() {

        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // When
        userServiceImpl.removeUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Remove User Fail")
    void removeUser_ShouldThrowException_WhenUSerNotFound() {

        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When and Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userServiceImpl.removeUser(userId);
        });

        assertEquals("404 NOT_FOUND \"User not found.\"", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }
}