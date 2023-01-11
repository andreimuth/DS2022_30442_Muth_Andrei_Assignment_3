package com.example.assignment1.mappers;

import com.example.assignment1.dto.DeviceDto;
import com.example.assignment1.entities.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceDto toDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .name(device.getName())
                .description(device.getDescription())
                .address(device.getAddress())
                .maximumHourlyEnergyConsumption(device.getMaximumHourlyEnergyConsumption())
                .ownerUsername(device.getOwner().getUsername())
                .build();
    }

    public Device fromDto(DeviceDto device) {
        return Device.builder()
                .name(device.getName())
                .description(device.getDescription())
                .address(device.getAddress())
                .maximumHourlyEnergyConsumption(device.getMaximumHourlyEnergyConsumption())
                .build();
    }

}
