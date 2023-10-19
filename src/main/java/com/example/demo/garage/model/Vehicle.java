package com.example.demo.garage.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Entity(name = "com.example.demo.raz.garage.Vehicle")
@Table(name = "vehicle")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle implements Electric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = true)
    private Integer year;

    @Column(nullable = true, name = "cubic_capacity")
    private Integer cubeCapacity;

    @Column
    private Boolean electric;

    @Column(nullable = false, name = "license_plate", unique = true, length = 20)
    private String licensePlate;

    @Column(nullable = false)
    private Boolean unregistered;

    @Column(nullable = false, name = "registration_date")
    private LocalDate registrationDate;


    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Access> accesses;

    @Override
    public Boolean isElectric() {
        return null;
    }
}
