package com.example.assignment1.services;

import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.EnergyConsumption;
import com.example.assignment1.repositories.EnergyConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnergyConsumptionService {

    private final EnergyConsumptionRepository energyConsumptionRepository;

    public List<Float> getEnergyConsumption(Device device, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findByDevice(device)
                .stream().filter(energyConsumption -> energyConsumption.getTimestamp().toLocalDate().equals(localDate))
                .collect(Collectors.toList());
        List<Float> consumptions = new ArrayList<>();

        //take Energy Consumption for each hour
        for (int i = 0; i < 24; i++) {
            int hour = i;
            List<Float> consumptionForHour = energyConsumptions.stream()
                    .filter(energyConsumption -> energyConsumption.getTimestamp().getHour() == hour)
                    .map(EnergyConsumption::getConsumption)
                    .collect(Collectors.toList());
            Float consumption = consumptionForHour.stream().reduce(0f, Float::sum);
            consumptions.add(consumption);
        }

        return consumptions;
    }
}
