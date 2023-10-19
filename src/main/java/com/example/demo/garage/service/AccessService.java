package com.example.demo.garage.service;

import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.garage.service.VehicleService.CUSTOM_FORMATTER;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@Data
@Component
@Transactional
public class AccessService {

    private final static String NO_MORE_SPACE = "NO MORE VEHICLES PLEASE; THE PARKING LOT IS FULL!!";
    private final static LocalDateTime openFrom = LocalDateTime.of(2023, 1, 1, 8, 0);
    private final static LocalDateTime openUntil = LocalDateTime.of(2023, 12, 31, 23, 59);
    public final static double minuteRate = 0.05;
    public final static int dayRate = 25;
    private static final java.text.DecimalFormat DecimalFormat = new DecimalFormat("0.00");

    private Vehicle[] parkedVehicles = new Vehicle[15];
    private int freeSpots = 15;


    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private VehicleRepository vehicleRepository;


    @PostConstruct
    private void postConstruct() {
        List<Access> parkedVehiclesRecords = accessRepository.findByAccessComplete(false);
        for(Access access : parkedVehiclesRecords) {
            int parkedSpot = access.getParkingSpot();
            Vehicle parkedVehicle = access.getVehicle();
            parkedVehicles[parkedSpot] = parkedVehicle;
        }
    }


    public Access registerVehicleAndPark(Vehicle vehicle, Integer parkingSpot) {
        vehicle.setRegistrationDate(LocalDate.now());
        vehicle.setUnregistered(false);
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("\n The vehicle has been correctly registered !! \n");
        return parkVehicleIfPossible(vehicle.getId(), parkingSpot);
    }


    public Access parkVehicleIfPossible(Long vehicleId, Integer parkingSpot) {

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if(!vehicle.isPresent()) {
            System.out.println("\n There is no vehicle with the id " + vehicleId + "! \n");
            return null;
        }

        Vehicle vehicleToPark = vehicle.get();
        if(vehicleToPark.getUnregistered()) {
            System.out.println("\n This vehicle (id: " + vehicleId + ") is not registered, you cannot park it! \n");
            return null;
        }

        //LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now();
        vehicleToPark.setRegistrationDate(LocalDate.now());
        List<Access> accesses = vehicleToPark.getAccesses();

        if( !CollectionUtils.isEmpty(accesses) ) {
            for(Access access : accesses) {
                if(!access.getAccessComplete()) {
                    System.out.println("\n This vehicle is already parked in" + access.getParkingSpot() + " from " + access.getParkingDateTime() + "! \n");
                    return null;
                }
            }
        }

        if(now.isBefore(openFrom) || now.isAfter(openUntil)) {
            System.out.println("\n The parking lot is closed, return at 8:00 until 23:59! \n");
            return null;
        }
        if(parkingSpot > 14 || parkingSpot < 0) {
            System.out.println("\n Parking Spot number not valid" + parkingSpot + "! \n");
            return null;
        }
        if(this.freeSpots == 0) {
            System.out.println(NO_MORE_SPACE);
            return null;
        }
        if(this.parkedVehicles[parkingSpot] != null) {
            System.out.println("\n You cannot park here, this place " + parkingSpot + " is already occupied!! \n");
            return null;
        } else {
            Access access = Access.builder()
                    .parkingDateTime(now)
                    .vehicle(vehicleToPark)
                    .parkingSpot(parkingSpot)
                    .accessComplete(false)
                    .build();

            access = this.accessRepository.save(access);
            this.parkedVehicles[parkingSpot] = vehicleToPark;

            System.out.println("\n You parked your " + vehicleToPark.getClass().getSimpleName() + " at: " + now.format(CUSTOM_FORMATTER) + ". Have a good day! \n");
            this.freeSpots--;
            return access;
        }
    }


    public Access removeVehicle(Long vehicleId) {
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

        Integer parkingSpot = incompleteAccess.getParkingSpot();
        Vehicle outGoingVehicle = parkedVehicles[parkingSpot];
        parkedVehicles[parkingSpot] = null;
        System.out.println("\n You have removed your " + outGoingVehicle.getClass().getSimpleName() + ". \n");

        incompleteAccess.setLeavingDateTime(LocalDateTime.now());
        String moneyToPay = calculateAmount(incompleteAccess);

        incompleteAccess.setAmountToPay(moneyToPay);
        System.out.println("\n Final price: " + moneyToPay + "$" + ". Have a good day. \n");
        incompleteAccess.setAccessComplete(true);
        Access completeAccess = this.accessRepository.save(incompleteAccess);

        this.freeSpots++;
        return completeAccess;
    }


    private String calculateAmount(Access access) {
        LocalDateTime parkingDateTime = access.getParkingDateTime();
        LocalDateTime leavingDateTime = access.getLeavingDateTime();
        long day = DAYS.between(parkingDateTime, leavingDateTime);
        long difference = MINUTES.between(parkingDateTime, leavingDateTime);
        if(day >= 1) {
            String moneyToPay = DecimalFormat.format(day * dayRate);
            return moneyToPay;
        } else {
            String moneyToPay = DecimalFormat.format(difference * minuteRate);
            return moneyToPay;
        }
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
