package com.example.demo.garage.service;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.garage.service.VehicleService.CUSTOM_FORMATTER;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@Component
@Data
public class Garage {

    private final static String NO_MORE_SPACE = "NO MORE VEHICLES PLEASE; THE PARKING LOT IS FULL!!";
    private final static LocalDateTime openFrom = LocalDateTime.of(2023, 1, 1, 8, 0);
    private final static LocalDateTime openUntil = LocalDateTime.of(2023, 12, 31, 23, 59);
    public Vehicle[] parkedVehicles = new Vehicle[15];
    public int freeSpots = 15;
    public final static double minuteRate = 0.05;
    public final static int dayRate = 25;
    private static final java.text.DecimalFormat DecimalFormat = new DecimalFormat("0.00");



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


    public Boolean checkIfParkingIsPossible(LocalDateTime now, Integer parkingSpot) {

        if (now.isBefore(openFrom) || now.isAfter(openUntil)) {
            System.out.println("\n The parking lot is closed, return at 8:00 until 23:59! \n");
            return false;
        }

        if (parkingSpot > 14 || parkingSpot < 0) {
            System.out.println("\n Parking Spot number not valid" + parkingSpot + "! \n");
            return false;
        }
        if (this.freeSpots == 0) {
            System.out.println(NO_MORE_SPACE);
            return false;
        }
        if (this.parkedVehicles[parkingSpot] != null) {
            System.out.println("\n You cannot park here, this place " + parkingSpot + " is already occupied!! \n");
            return false;
        }

        return true;
    }


    public String calculateAmount(Access access) {
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


    public String findAccessLength(Access access) {

        LocalDateTime parkingDateTime = access.getParkingDateTime();
        LocalDateTime leavingDateTime = access.getLeavingDateTime();

        long difference = MINUTES.between(parkingDateTime, leavingDateTime);

        if(difference == 0) {
            return "<1m";
        }

        long days = difference / (60 * 24);
        long hours = (difference - (days * (60 * 24))) / 60;
        long minutes = (difference - days) % 60;

        String timeLength = "";

        if(minutes != 0) {
            timeLength = minutes + "m";
        }
        if(hours != 0) {
            timeLength = hours + "h " + timeLength;
        }
        if(days != 0) {
            timeLength = days + "d " + timeLength;
        }

        return timeLength;
    }
}
