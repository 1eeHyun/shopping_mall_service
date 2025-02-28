package com.ldh.shoppingmall.service;

import com.ldh.shoppingmall.dto.UserDto;
import com.ldh.shoppingmall.entity.User;
import com.ldh.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a user
     * @param userDto
     * @return new UserDto object with saved user and password
     */
    public UserDto register(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new IllegalStateException("Username already exists.");

        User user = new User(userDto.getUsername(),
                             passwordEncoder.encode(userDto.getUsername()),
                             User.Role.USER,
                             LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return new UserDto(savedUser.getUsername(), savedUser.getPassword());
    }

    /**
     * Remove a user
     * @param userId
     * @return ResponseEntity.ok
     */
    public ResponseEntity<String> remove(Long userId) {

        if (!userRepository.existsById(userId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

        userRepository.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
