package com.example.assignment1.services;

import com.example.assignment1.entities.ERole;
import com.example.assignment1.entities.Role;
import com.example.assignment1.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String role) {
        return roleRepository.findByName(ERole.valueOf(role))
                .orElseThrow(() -> new RuntimeException("Role " + role + " not found"));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

}
