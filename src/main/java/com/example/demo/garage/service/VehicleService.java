package com.example.demo.garage.service;

import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Data
@Service
public class VehicleService {

    public final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private VehicleRepository vehicleRepository;


    public Optional<Vehicle> findVehicleByLicensePlate(String licensePlate) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        String message = "You found a vehicle (see this response's body) !!";
        System.out.println(message);
        return vehicle;
    }


    public Vehicle registerVehicle(Vehicle vehicle) {
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("The user has been correctly registered (see this response's body) !!");
        return vehicle;
    }
}
