package com.example.assignment1.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.hibernate.annotations.OnDeleteAction.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private String address;
    private Float maximumHourlyEnergyConsumption;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = CASCADE)
    private User owner;

}
