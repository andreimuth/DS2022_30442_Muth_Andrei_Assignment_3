package com.example.assignment1.repositories;

import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findById(Long id);
    List<Device> findByOwner(User user);
}
