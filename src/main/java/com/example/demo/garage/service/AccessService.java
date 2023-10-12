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

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
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

    // to remove
    private Pair<Vehicle, Access> test(Vehicle vehicle) {
        return Pair.of(null, null);
    }


    public Access registerVehicleAndPark(Vehicle vehicle, Integer parkingSpot) {
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("The vehicle has been correctly registered !!");
        return parkVehicleIfPossible(vehicle.getId(), parkingSpot);
    }


    public Access parkVehicleIfPossible(Long vehicleId, Integer parkingSpot) {

        Vehicle vehicle = vehicleRepository.getById(vehicleId);

        //LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now();

        List<Access> accesses = vehicle.getAccesses();

        for(Access access : accesses) {
            if(!access.getAccessComplete()) {
                System.out.println("\n This vehicle is already parked \n");
                return null;
            }
        }
        if(now.isBefore(openFrom) || now.isAfter(openUntil)) {
            System.out.println("The parking lot is closed, return at 8:00 until 23:59 ");
            return null;
        }
        if(parkingSpot > 14 || parkingSpot < 0) {
            System.out.println("Parking Spot number not valid!! ");
            return null;
        }
        if(this.freeSpots == 0) {
            System.out.println(NO_MORE_SPACE);
            return null;
        }
        if(this.parkedVehicles[parkingSpot] != null) {
            System.out.println("You can not park Here!! ");
            return null;
        } else {
            Access access = Access.builder()
                    .parkingDateTime(now)
                    .parkingDateTimeStringFormat(now.format(CUSTOM_FORMATTER))
                    .vehicle(vehicle)
                    .parkingSpot(parkingSpot)
                    .accessComplete(false)
                    .build();

            access = this.accessRepository.save(access);
            this.parkedVehicles[parkingSpot] = vehicle;

            System.out.println("You parked your " + vehicleId.getClass().getSimpleName() + " at: " + now + ". Have a good day! ");
            this.freeSpots--;
            return access;
        }
    }


    public Access removeVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId).get();
        List<Access> accesses = vehicle.getAccesses();

        // finds the non complete access, if it exists
        Access incompleteAccess = null;
        for(Access access : accesses) {
            if (!access.getAccessComplete()) {
                incompleteAccess = access;
            }
        }
        if(incompleteAccess == null) {
            System.out.println("This vehicle is not parked! ");
            return incompleteAccess;
        }

        Integer parkingSpot = incompleteAccess.getParkingSpot();
        Vehicle outGoingVehicle = parkedVehicles[parkingSpot];
        parkedVehicles[parkingSpot] = null;
        System.out.println("You have removed your " + outGoingVehicle.getClass().getSimpleName());

        incompleteAccess.setLeavingDateTime(LocalDateTime.now());
        String moneyToPay = calculateAmount(incompleteAccess);

        incompleteAccess.setAmountToPay(moneyToPay);
        System.out.println("Final price: " + moneyToPay + "$" + ". Have a good day. ");
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


    public Optional<Access> findAccess(Long id) {
        String message = "You asked to see an access by id (see this response's body) !!";
        System.out.println(message);
        return this.accessRepository.findById(id);
    }


    public List<Access> findAccessesByLicensePlates(String licensePlate) {
        String message = "your have parked (see this response's body) !!";
        System.out.println(message);
        return vehicleRepository.findByLicensePlate(licensePlate).get().getAccesses();
    }
}
