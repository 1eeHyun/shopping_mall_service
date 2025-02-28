package com.ldh.shoppingmall.service;

import com.ldh.shoppingmall.dto.UserDto;
import com.ldh.shoppingmall.entity.User;
import com.ldh.shoppingmall.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.ldh.shoppingmall.entity.User.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mock Repository

    @Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder

    @InjectMocks
    private UserService userService; // Object that is tested

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this); // initialize Mock object
    }

    @Test
    @DisplayName("Register Success")
    void register_ShouldSaveUser_WhenUsernameIsUnique() {

        // Given
        UserDto userDto = new UserDto("newUser", "password");
        User mockSaveduser = new User(userDto.getUsername(), "encodedPassword", Role.USER, LocalDateTime.now());

        // No exists username
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        // Encoded password Mock
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(mockSaveduser);

        // when
        userService.register(userDto);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Register Fails When Username Already Exsists")
    void register_Should_Not_SaveUser_WhenUsernameIsUnique() {

        // Given
        UserDto userDto = new UserDto("existingUser", "password");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // When and Then
        assertThatThrownBy(() -> userService.register(userDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Username already exists.");


        verify(userRepository, never()).save(any(User.class));
    }
}