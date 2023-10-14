package com.example.demo.garage.controller;

import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.service.VehicleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Data
public class VehicleController {

    @Autowired
    VehicleService vehicleService;


    @GetMapping("/find_vehicle_by_license_plate/{license_plate}")
    public ResponseEntity<Optional<Vehicle>> findVehicleByLicensePlate(@PathVariable ("license_plate") String licensePlate) {
        Optional<Vehicle> vehicleFound = vehicleService.findVehicleByLicensePlate(licensePlate);
        return new ResponseEntity<>(vehicleFound, HttpStatus.OK);
    }


    @PostMapping("/add_vehicle")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Vehicle vehicle) {
        Vehicle submittedVehicle = vehicleService.registerVehicle(vehicle);
        return new ResponseEntity<>(submittedVehicle, HttpStatus.OK);
    }

    @DeleteMapping("/unregister_vehicle")
    public ResponseEntity<Vehicle> unregisterVehicle(@RequestParam("vehicle_id") Long vehicleId) {
        Vehicle unregisteredVehicle = vehicleService.unregisterVehicle(vehicleId);
        return new ResponseEntity<>(unregisteredVehicle, HttpStatus.OK);
    }
}
