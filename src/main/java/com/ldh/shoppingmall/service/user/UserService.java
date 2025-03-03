package com.ldh.shoppingmall.service.user;

import com.ldh.shoppingmall.dto.user.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDto register(UserDto userDto);
    void removeUser(Long userId);

    Long findUserIdByUsername(UserDetails userDetails);
}
