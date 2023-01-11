package com.example.assignment1.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnergyConsumption {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    @OnDelete(action = CASCADE)
    private Device device;
    private LocalDateTime timestamp;
    private Float consumption;

}
