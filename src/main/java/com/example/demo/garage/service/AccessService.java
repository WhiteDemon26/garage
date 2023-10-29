package com.example.demo.garage.service;

import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.garage.service.VehicleService.CUSTOM_FORMATTER;

@Data
@Component
@Transactional
public class AccessService {


    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private Garage garage;



    public Access createNewAccessIn(Long vehicleId, Integer parkingSpot) {

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if(!vehicle.isPresent()) {
            System.out.println("\n There is no vehicle with the id " + vehicleId + "! \n");
            return null;
        }

        Vehicle vehicleToPark = vehicle.get();
        if(garage.checkIfVehicleIsRegisteredOrParked(vehicleToPark)) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        if (garage.checkIfParkingIsPossible(now, parkingSpot)) {

            Access access = Access.builder()
                                    .parkingDateTime(now)
                                    .vehicle(vehicleToPark)
                                    .parkingSpot(parkingSpot)
                                    .accessComplete(false)
                                    .build();
            access = this.accessRepository.save(access);

            garage.parkVehicle(vehicleToPark, parkingSpot, now);
            return access;
        } else {
            return null;
        }
    }


    public Access completeAccessOut(Long vehicleId) {

        Optional<Vehicle> vehicle = this.vehicleRepository.findById(vehicleId);
        if(!vehicle.isPresent()) {
            System.out.println("\n There is no vehicle with the id " + vehicleId + "! \n");
            return null;
        }
        List<Access> accesses = vehicle.get().getAccesses();

        // finds the non-complete access, if it exists
        Access incompleteAccess = null;
        for(Access access : accesses) {
            if (!access.getAccessComplete()) {
                incompleteAccess = access;
            }
        }
        if(incompleteAccess == null) {
            System.out.println("\n This vehicle (id: " + vehicleId + ") is not parked! \n");
            return incompleteAccess;
        }

        Vehicle outGoingVehicle = garage.removeVehicle(incompleteAccess);

        incompleteAccess.setLeavingDateTime(LocalDateTime.now());
        String moneyToPay = garage.calculateAmount(incompleteAccess);

        String accessLength = garage.findAccessLength(incompleteAccess);
        System.out.println("\n You parked your " + outGoingVehicle.getClass().getSimpleName() + " for " + accessLength + " time. \n");

        incompleteAccess.setAccessLength(accessLength);
        incompleteAccess.setAmountToPay(moneyToPay);
        System.out.println("\n Final price: " + moneyToPay + "$" + ". Have a good day. \n");
        incompleteAccess.setAccessComplete(true);
        Access completeAccess = this.accessRepository.save(incompleteAccess);

        garage.freeSpots++;
        return completeAccess;
    }


    public Access findAccess(Long id) {

        Optional<Access> access = this.accessRepository.findById(id);
        if(!access.isPresent()) {
            System.out.println("\n There is no access record with the id " + id + "! \n");
            return null;
        }
        String message = "\n You asked to see an access by id (see this response's body) !! \n";
        System.out.println(message);
        return access.get();
    }


    public List<Access> findAccessesByLicensePlates(String licensePlate) {

        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        if(!vehicle.isPresent()) {
            System.out.println("\n There is no accesses with the license plate " + licensePlate + "! \n");
            return Collections.emptyList();
        }
        String message = "\n You asked to see all your accesses by id (see this response's body) !! \n";
        System.out.println(message);

        return vehicle.get().getAccesses();
    }
}
