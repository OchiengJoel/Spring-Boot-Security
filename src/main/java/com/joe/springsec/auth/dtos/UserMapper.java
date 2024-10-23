package com.joe.springsec.auth.dtos;

import com.joe.springsec.auth.models.User;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        // Do not include password for security reasons
        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Set password only when creating a new user
        return user;
    }
}
