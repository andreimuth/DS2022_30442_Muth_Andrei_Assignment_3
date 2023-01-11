package com.example.assignment1.entities;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;
    @Enumerated(STRING)
    @Column
    private ERole name;

}
