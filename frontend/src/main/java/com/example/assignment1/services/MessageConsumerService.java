package com.example.assignment1.services;

import com.example.assignment1.entities.Device;
import com.example.assignment1.entities.EnergyConsumption;
import com.example.assignment1.entities.User;
import com.example.assignment1.repositories.DeviceRepository;
import com.example.assignment1.repositories.EnergyConsumptionRepository;
import com.example.assignment1.repositories.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneId.systemDefault;

@Service
@RequiredArgsConstructor
public class MessageConsumerService {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);

    private final EnergyConsumptionRepository energyConsumptionRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @RabbitListener(queues = "appSpecificQueue")
    public void receiveMessage(final String customMessage) {
        log.info("Received message and deserialized to 'CustomMessage': {}", customMessage);
        try {
            Long timestamp = Long.parseLong(new Gson().fromJson(customMessage, JsonObject.class).get("timestamp").toString());
            Long deviceID = Long.parseLong(new Gson().fromJson(customMessage, JsonObject.class).get("device_id").toString());
            Float consumption = Float.parseFloat(new Gson().fromJson(customMessage, JsonObject.class).get("measurement_value").toString());
            LocalDateTime date = LocalDateTime.ofInstant(ofEpochMilli(timestamp), systemDefault());
            Device device = getDevice(deviceID);
            Float consumptionForLastHour = getTotalConsumptionForLastHour(device, date.getHour());

            if(consumptionForLastHour + consumption > device.getMaximumHourlyEnergyConsumption()) {
                User user = device.getOwner();
                messagingTemplate.convertAndSend("/topic/notify/user/" + user.getId(), "Too much consumption " +
                        "for device " + device.getName() + "!");
            }
            energyConsumptionRepository.save(EnergyConsumption.builder()
                    .timestamp(date)
                    .device(device)
                    .consumption(consumption)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Device getDevice(Long deviceID) {
        return deviceRepository.findById(deviceID).orElseThrow(() -> new RuntimeException("Device with id = " + deviceID +  " not found"));
    }

    private Float getTotalConsumptionForLastHour(Device device, int hour) {
        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository.findByDevice(device).stream()
                .filter(energyConsumption -> energyConsumption.getTimestamp().getHour() == hour)
                .collect(Collectors.toList());
        return energyConsumptions.stream().map(EnergyConsumption::getConsumption).reduce(0f, Float::sum);
    }

}
