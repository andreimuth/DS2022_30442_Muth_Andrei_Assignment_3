package com.example.assignment1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class DeviceDto {
    private Long id;
    @NotEmpty(message = "Name is mandatory")
    private String name;
    @NotEmpty(message = "Description is mandatory")
    private String description;
    @NotEmpty(message = "Address is mandatory")
    private String address;
    @NotNull(message = "Maximum hourly energy consumption is mandatory")
    @Min(value = 0, message = "Maximum hourly energy consumption must be greater than or equal to 0")
    private Float maximumHourlyEnergyConsumption;
    @NotEmpty(message = "Owner username is mandatory")
    private String ownerUsername;
}
