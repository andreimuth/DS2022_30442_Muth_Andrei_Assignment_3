package com.example.assignment1.controllers;

import com.example.assignment1.entities.Device;
import com.example.assignment1.services.DeviceService;
import com.example.assignment1.services.EnergyConsumptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.assignment1.UrlMapping.ENERGY_CONSUMPTION;
import static com.example.assignment1.UrlMapping.GET_CONSUMPTION_FOR_DEVICE;

@RestController
@RequestMapping(ENERGY_CONSUMPTION)
@RequiredArgsConstructor
public class EnergyConsumptionController {

    private final EnergyConsumptionService energyConsumptionService;
    private final DeviceService deviceService;
    
    @GetMapping(GET_CONSUMPTION_FOR_DEVICE)
    public List<Float> getConsumption(@PathVariable Long id, @PathVariable String date) {
        Device device = deviceService.findById(id);
        return energyConsumptionService.getEnergyConsumption(device, date);
    }

}
