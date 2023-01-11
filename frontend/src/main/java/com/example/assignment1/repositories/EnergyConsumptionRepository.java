package com.example.assignment1.repositories;

import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.EnergyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyConsumptionRepository extends JpaRepository<EnergyConsumption, Long> {

    List<EnergyConsumption> findByDevice(Device device);

}


