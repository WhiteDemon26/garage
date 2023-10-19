package com.example.demo.garage.controller;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.service.AccessService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Data
public class AccessController {

    @Autowired
    AccessService accessService;


    @GetMapping("/find_access_by_id/{access_id}")
    public ResponseEntity<Access> findAccess(@PathVariable("access_id") Long accessId) {
        Access fondAccess = accessService.findAccess(accessId);
        return new ResponseEntity<>(fondAccess, HttpStatus.OK);
    }


    @PostMapping("/register_vehicle_and_park")
    public ResponseEntity<Access> registerVehicleAndPark(@RequestBody Vehicle vehicle, @RequestParam("parking_spot") Integer parkingSpot) {
        Access newAccess = accessService.registerVehicleAndPark(vehicle, parkingSpot);
        return new ResponseEntity<>(newAccess, HttpStatus.OK);
    }


    @PostMapping("/park_vehicle")
    public ResponseEntity<Access> addParkedVehicle(@RequestParam("vehicle_id") Long vehicleId, @RequestParam("parking_spot") Integer parkingSpot) {
        Access parkedVehicle = accessService.parkVehicleIfPossible(vehicleId, parkingSpot);
        return new ResponseEntity<>(parkedVehicle, HttpStatus.OK);
    }


    @PutMapping("/remove_vehicle/{vehicle_id}")
    public ResponseEntity<Access> removeParkedVehicle(@PathVariable("vehicle_id") Long vehicleId) {
        Access leavingVehicle = accessService.removeVehicle(vehicleId);
        return new ResponseEntity<>(leavingVehicle, HttpStatus.OK);
    }


    @GetMapping("/show_parked_times_by_license_plate/{license_plate}")
    public ResponseEntity<List<Access>> showTimesParkedByLicensePlate(@PathVariable("license_plate") String licensePlate) {
        List<Access> timesParkedByLicensePlate = accessService.findAccessesByLicensePlates(licensePlate);
        return new ResponseEntity<>(timesParkedByLicensePlate, HttpStatus.OK);
    }
}
