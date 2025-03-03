package com.ldh.shoppingmall.service.user;

import com.ldh.shoppingmall.dto.user.UserDto;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a user
     * @param userDto
     * @return new UserDto object with saved user (without password)
     */
    @Override
    public UserDto register(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");

        User user = new User(userDto.getUsername(),
                             passwordEncoder.encode(userDto.getPassword()),
                             User.Role.USER);

        userRepository.save(user);
        return new UserDto(userDto.getUsername(), null);
    }

    /**
     * Remove a user
     * @param userId
     * @return ResponseEntity.ok
     */
    @Override
    public void removeUser(Long userId) {

        if (!userRepository.existsById(userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");

        userRepository.deleteById(userId);
    }

    /**
     * Retrieves the user ID based on the provided UserDetails.
     */
    @Override
    public Long findUserIdByUsername(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
