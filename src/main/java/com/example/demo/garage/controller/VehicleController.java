package com.example.demo.garage.controller;

import com.example.demo.garage.model.Access;
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
    public ResponseEntity<Vehicle> findVehicleByLicensePlate(@PathVariable ("license_plate") String licensePlate) {
        Vehicle vehicleFound = vehicleService.findVehicleByLicensePlate(licensePlate);
        return new ResponseEntity<>(vehicleFound, HttpStatus.OK);
    }


    @PostMapping("/register_vehicle")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Vehicle vehicle) {
        Vehicle submittedVehicle = vehicleService.registerVehicle(vehicle);
        return new ResponseEntity<>(submittedVehicle, HttpStatus.OK);
    }


    @PostMapping("/register_vehicle_and_park")
    public ResponseEntity<Access> registerVehicleAndPark(@RequestBody Vehicle vehicle, @RequestParam("parking_spot") Integer parkingSpot) {
        Access newAccess = vehicleService.registerVehicleAndPark(vehicle, parkingSpot);
        return new ResponseEntity<>(newAccess, HttpStatus.OK);
    }


    @PutMapping("/re_register_vehicle/{license_plate}")
    public ResponseEntity<Vehicle> reRegisterVehicle(@PathVariable ("license_plate") String licensePlate) {
        Vehicle reSubmittedVehicle = vehicleService.reRegisterVehicle(licensePlate);
        return new ResponseEntity<>(reSubmittedVehicle, HttpStatus.OK);
    }


    @PutMapping("/unregister_vehicle/{vehicle_id}")
    public ResponseEntity<Vehicle> unregisterVehicle(@PathVariable ("vehicle_id") Long vehicleId) {
        Vehicle unregisteredVehicle = vehicleService.unregisterVehicle(vehicleId);
        return new ResponseEntity<>(unregisteredVehicle, HttpStatus.OK);
    }
}
