package com.example.demo.garage.model.vehicles;

import com.example.demo.garage.model.Vehicle;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;

@SuperBuilder
@Data
public class Car extends Vehicle {

    @Transient
    private Integer porte;

    @Transient
    private String alimentazione;
}
