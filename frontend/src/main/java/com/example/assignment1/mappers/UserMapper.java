package com.example.assignment1.mappers;

import com.example.assignment1.dto.UserDto;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder encoder;

    public User fromDto(UserDto userDto, Role role) {
        return User.builder()
                .username(userDto.username)
                .password(encoder.encode(userDto.getPassword()))
                .role(role)
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().getName().toString())
                .build();
    }

}
