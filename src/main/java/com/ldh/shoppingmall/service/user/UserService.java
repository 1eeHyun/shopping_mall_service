package com.ldh.shoppingmall.service.user;

import com.ldh.shoppingmall.dto.user.UserDto;

public interface UserService {

    UserDto register(UserDto userDto);
    void removeUser(Long userId);
}
