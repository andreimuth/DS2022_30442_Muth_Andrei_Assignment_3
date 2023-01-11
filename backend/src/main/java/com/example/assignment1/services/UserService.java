package com.example.assignment1.services;

import com.example.assignment1.dto.UserDto;
import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.ERole;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import com.example.assignment1.repositories.RoleRepository;
import com.example.assignment1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.assignment1.entities.ERole.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id = " + id + " not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existingUser = findById(id);
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setRole(user.getRole());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> findNonAdminUsers() {
        Role role = roleRepository.findByName(USER)
                .orElseThrow(() -> new RuntimeException("Role " + USER + "  not found"));
        return userRepository.findByRole(role);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByUsernameAndIdNot(String username, Long id) {
        return userRepository.existsByUsernameAndIdNot(username, id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username = " + username + " not found"));
    }

    public User findAdmin() {
        Role role = roleRepository.findByName(ERole.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role " + ERole.ADMIN + "  not found"));
        return userRepository.findByRole(role).stream().findFirst().orElseThrow(() -> new RuntimeException("Admin not found"));
    }

}
