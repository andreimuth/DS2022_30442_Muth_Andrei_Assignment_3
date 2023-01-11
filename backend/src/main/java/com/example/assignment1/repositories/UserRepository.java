package com.example.assignment1.repositories;

import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.Role;
import com.example.assignment1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByUsernameAndIdNot(String username, Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> findByRole(Role role);
}
