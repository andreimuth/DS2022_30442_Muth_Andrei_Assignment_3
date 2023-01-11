package com.example.assignment1.services;

import com.example.assignment1.dto.DeviceDto;
import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.User;
import com.example.assignment1.repositories.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Device findById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Device with id = " + id + " not found"));
    }

    public Device createDevice(Device device, User user) {
        device.setOwner(user);
        return deviceRepository.save(device);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public Device updateDevice(Long id, DeviceDto deviceDto, User user) {
        Device device = findById(id);
        device.setName(deviceDto.getName());
        device.setDescription(deviceDto.getDescription());
        device.setAddress(deviceDto.getAddress());
        device.setMaximumHourlyEnergyConsumption(deviceDto.getMaximumHourlyEnergyConsumption());
        device.setOwner(user);
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    public List<Device> findByOwner(User user) {
        return deviceRepository.findByOwner(user);
    }

}
