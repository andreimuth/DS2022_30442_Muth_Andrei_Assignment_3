package com.example.assignment1.controllers;

import com.example.assignment1.dto.DeviceDto;
import com.example.assignment1.entities.User;
import com.example.assignment1.mappers.DeviceMapper;
import com.example.assignment1.services.DeviceService;
import com.example.assignment1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.assignment1.UrlMapping.*;

@RestController
@RequestMapping(DEVICES)
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;
    private final UserService userService;

    @PostMapping(CREATE_DEVICE)
    public void createDevice(@Valid @RequestBody DeviceDto deviceDto) {
        User user = userService.findByUsername(deviceDto.getOwnerUsername());
        deviceService.createDevice(deviceMapper.fromDto(deviceDto), user);
    }

    @GetMapping(FIND_ALL)
    public List<DeviceDto> getAllDevices() {
        return deviceService.getAllDevices().stream()
                .map(deviceMapper::toDto).collect(Collectors.toList());
    }

    @PutMapping(UPDATE_DEVICE)
    public void updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceDto deviceDto) {
        User user = userService.findByUsername(deviceDto.getOwnerUsername());
        deviceService.updateDevice(id, deviceDto, user);
    }

    @DeleteMapping(DELETE_DEVICE)
    public void deleteMapping(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }

    @GetMapping(FIND_BY_OWNER)
    public List<DeviceDto> findByOwner(@PathVariable Long id) {
        User user = userService.findById(id);
        return deviceService.findByOwner(user).stream()
                .map(deviceMapper::toDto).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex) {
        return Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
    }

}
