package com.ldh.shoppingmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldh.shoppingmall.controller.user.AuthController;
import com.ldh.shoppingmall.dto.user.UserDto;
import com.ldh.shoppingmall.security.JwtTokenProvider;
import com.ldh.shoppingmall.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("Sign-Up Success")
    void SignUp_Success() throws Exception {

        UserDto userDto = new UserDto("testuser", "password123");

        when(userServiceImpl.register(any(UserDto.class))).thenReturn(new UserDto("testuser", null));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userServiceImpl, times(1)).register(any(UserDto.class));
    }

    @Test
    @DisplayName("Delete User Sussess")
    void Delete_User_Success() throws Exception {

        doNothing().when(userServiceImpl).removeUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auth/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));

        verify(userServiceImpl, times(1)).removeUser(1L);
    }

    @Test
    @DisplayName("Log-In Success")
    void LogIn_Success() throws Exception {

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testuser");
        loginRequest.put("password", "password123");

        // Create mock UserDetails object
        UserDetails userDetails = User.withUsername("testuser")
                .password("password123")
                .roles("USER")
                .build();

        // Create mock Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // When authenticationManager calls authenticate(), Return Authentication object above
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Set Mock to generate JWT token
        when(jwtTokenProvider.generateToken(any())).thenReturn("mocked-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
}