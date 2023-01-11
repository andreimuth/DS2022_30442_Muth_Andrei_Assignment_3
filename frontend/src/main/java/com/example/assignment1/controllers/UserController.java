package com.example.assignment1.controllers;

import com.example.assignment1.dto.UserDto;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import com.example.assignment1.mappers.UserMapper;
import com.example.assignment1.services.RoleService;
import com.example.assignment1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.assignment1.UrlMapping.*;

@RestController
@RequestMapping(USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;

    @PostMapping(CREATE_USER)
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto) {
        if(userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Username is already taken!");
        }
        Role role = roleService.findByName(userDto.getRole());
        userService.createUser(userMapper.fromDto(userDto, role));
        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping(FIND_ALL)
    public List<UserDto> findAll() {
        return userService.findAll().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    @PutMapping(UPDATE_USER)
    public ResponseEntity<String>  updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        if(userService.existsByUsernameAndIdNot(userDto.getUsername(), id)) {
            return ResponseEntity
                    .badRequest()
                    .body("Username is already taken!");
        }
        Role role = roleService.findByName(userDto.getRole());
        User user = userMapper.fromDto(userDto, role);
        userService.updateUser(id, user);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping(DELETE_USER)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping(FIND_NON_ADMINS)
    public List<UserDto> findNonAdminUsers() {
        return userService.findNonAdminUsers().stream()
                .map(userMapper::toDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println("Validation exception");
        return Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
    }

}
