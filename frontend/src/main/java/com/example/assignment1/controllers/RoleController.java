package com.example.assignment1.controllers;

import com.example.assignment1.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.assignment1.UrlMapping.FIND_ALL;
import static com.example.assignment1.UrlMapping.ROLES;

@RestController
@RequestMapping(ROLES)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(FIND_ALL)
    public List<String> getAllRoles() {
        return roleService.getAllRoles().stream().map(role -> role.getName().toString()).collect(Collectors.toList());
    }

}
