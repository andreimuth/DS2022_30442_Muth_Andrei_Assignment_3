package com.example.assignment1.security;

import com.example.assignment1.entities.ERole;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import com.example.assignment1.repositories.RoleRepository;
import com.example.assignment1.repositories.UserRepository;
import com.example.assignment1.security.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.assignment1.entities.ERole.*;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public void register(SignupRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Role role = roleRepository.findByName(USER)
                        .orElseThrow(() -> new RuntimeException("Cannot find role: " + USER));
        user.setRole(role);
        userRepository.save(user);
    }
}