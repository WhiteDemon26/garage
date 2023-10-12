package com.example.demo.garage.model.vehicles;

import com.example.demo.garage.model.Vehicle;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class Van extends Vehicle {

    private String capacity;
}
