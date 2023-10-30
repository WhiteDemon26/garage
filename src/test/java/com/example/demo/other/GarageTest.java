package com.example.demo.other;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.service.Garage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.demo.garage.service.Garage.DecimalFormat;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GarageTest {


    @Mock
    AccessRepository accessRepository;

    @InjectMocks
    public Garage garage;


    @Test
    void test() {

        Vehicle vehicleToPark = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();
        Access completeAccess = Access.builder()
                .parkingDateTime(null)
                .vehicle(vehicleToPark)
                .parkingSpot(5)
                .accessComplete(true)
                .build();
        List<Access> accesses = List.of(completeAccess);

        when(accessRepository.findByAccessComplete(false)).thenReturn(accesses);
        garage.postConstruct();

        assertEquals(14, garage.freeSpots);
        assertNotNull(garage.parkedVehicles[5]);
        assertEquals(garage.parkedVehicles[5].getLicensePlate(), vehicleToPark.getLicensePlate());
    }



    @Test
    void testRemoveVehicle() {

        Vehicle vehicleToPark = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();
        garage.parkedVehicles[5] = vehicleToPark;

        Access completeAccess = Access.builder()
                .parkingDateTime(LocalDateTime.now())
                .vehicle(vehicleToPark)
                .parkingSpot(5)
                .accessComplete(true)
                .build();
        Vehicle removedVehicle = garage.removeVehicle(completeAccess);

        assertNull(garage.parkedVehicles[5]);
        //System.out.println("\n You parked have a good day! \n");
    }


    @Test
    void testCheckIfVehicleIsRegisteredOrParked() {

        // cases to test:
        // 1 se il veicolo non è registrato
        // 2 se il veicolo è parcheggiato
        // 3 se il veicolo è registrato e non è parcheggiato

        Vehicle vehicleToPark = Vehicle.builder()
                                        .model("Mustang")
                                        .licensePlate("ARG-OOP-202")
                                        .unregistered(true)
                                        .registrationDate(LocalDate.now())
                                        .build();
        Boolean isVehicleRegistered = garage.checkIfVehicleIsRegisteredOrParked(vehicleToPark);

        assertTrue(isVehicleRegistered, "the vehicle is NOT registered");
        //System.out.println("\n Your vehicle is registered have a good day! \n");


        vehicleToPark.setUnregistered(false);

        Access incompleteAccess = Access.builder()
                                        .parkingDateTime(LocalDateTime.now())
                                        .vehicle(vehicleToPark)
                                        .parkingSpot(5)
                                        .accessComplete(false)
                                        .build();
        vehicleToPark.setAccesses(List.of(incompleteAccess));

        Boolean isVehicleParked = garage.checkIfVehicleIsRegisteredOrParked(vehicleToPark);

        assertTrue(isVehicleParked, "the vehicle is parked");


        Access completeAccess = Access
                .builder()
                .parkingDateTime(LocalDateTime.now())
                .vehicle(vehicleToPark)
                .parkingSpot(null)
                .accessComplete(true)
                .build();
        vehicleToPark.setAccesses(List.of(completeAccess));

        Boolean isVehicleRegisteredAndNotParked = garage.checkIfVehicleIsRegisteredOrParked(vehicleToPark);

        assertFalse(isVehicleRegisteredAndNotParked, "the vehicle is registered and NOT parked");
    }


    @Test
    void testCheckIfParkingIsPossible() {

        LocalDateTime hourWhenGarageIsClosed = LocalDateTime.of(2023, 1, 1, 5, 0);
        LocalDateTime hourWhenGarageIsOpen = LocalDateTime.of(2023, 1, 1, 9, 0);

        Boolean IsPossibleParkHere = garage.checkIfParkingIsPossible(hourWhenGarageIsOpen, 10);
        assertTrue(IsPossibleParkHere, "The garage is open and you can park here");
        System.out.println("\n You can park here have a good day! \n");

        Boolean isPossibleToPark = garage.checkIfParkingIsPossible(hourWhenGarageIsClosed, null);
        assertFalse(isPossibleToPark, "the time is wrong, the garage is closed");

        Boolean isPossibleToParkOutOfRange = garage.checkIfParkingIsPossible(hourWhenGarageIsOpen, 16);
        assertFalse(isPossibleToParkOutOfRange, "the parking spot doesn't exist in the garage");

        Random rand = new Random();
        int randomParkingSpot = rand.nextInt(15);
        garage.parkVehicle(new Vehicle(), randomParkingSpot, hourWhenGarageIsOpen);
        Boolean isPossibleToParkIfIsOccupied = garage.checkIfParkingIsPossible(hourWhenGarageIsOpen, randomParkingSpot);
        assertFalse(isPossibleToParkIfIsOccupied, "the parking spot " + randomParkingSpot + " is occupied, you can't park here");

        List<Vehicle> listOfVehicles = Collections.nCopies(15, new Vehicle());
        for(int parkingSpot = 0; parkingSpot < 15; parkingSpot++) {
            garage.parkVehicle(listOfVehicles.get(parkingSpot), parkingSpot, hourWhenGarageIsOpen);
        }
        randomParkingSpot = rand.nextInt(15);
        Boolean isPossibleToParkIfTheGarageIsFull = garage.checkIfParkingIsPossible(hourWhenGarageIsOpen, randomParkingSpot);
        assertFalse(isPossibleToParkIfTheGarageIsFull, "the garage is full and you can't park here");
    }


    @Test
    void testCalculateAmount() {

        Random rand = new Random();
        int dayOfMonth = rand.nextInt(28) + 1;

        LocalDateTime hourWhenVehicleParked = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, dayOfMonth, 10, 0);
        Access accessComplete = Access.builder()
                                        .parkingDateTime(hourWhenVehicleParked)
                                        .leavingDateTime(hourWhenVehicleWentAway)
                                        .build();

        String howMuchToPayForSomeDays = garage.calculateAmount(accessComplete);
        long day = DAYS.between(hourWhenVehicleParked, hourWhenVehicleWentAway);
        String checkMoneyToPayForTwoDays = DecimalFormat.format(day * 25);
        assertEquals(checkMoneyToPayForTwoDays, howMuchToPayForSomeDays);

        int minute = rand.nextInt(59) + 1;
        hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, 1, 10, minute);
        accessComplete.setLeavingDateTime(hourWhenVehicleWentAway);
        String howToPayForMinutes = garage.calculateAmount(accessComplete);
        long differenceOfMinutes = MINUTES.between(hourWhenVehicleParked, hourWhenVehicleWentAway);
        String checkMoneyToPayToPayForMinutes = DecimalFormat.format(differenceOfMinutes * 0.05);
        assertEquals(checkMoneyToPayToPayForMinutes, howToPayForMinutes);
    }


    @Test
    void testFindAccessLength() {

        LocalDateTime hourWhenVehicleParked = LocalDateTime.of(2023, 1, 1, 1, 0);
        LocalDateTime hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, 1, 1, 0, 23);
        Access accessComplete = Access.builder()
                                        .parkingDateTime(hourWhenVehicleParked)
                                        .leavingDateTime(hourWhenVehicleWentAway)
                                        .build();

        String accessLengthUnder1m = garage.findAccessLength(accessComplete);
        String checkPossibleAccessLength = "<1m";
        assertEquals(checkPossibleAccessLength, accessLengthUnder1m);

        Random rand = new Random();
        int dayOfMonth = rand.nextInt(28) + 1;

        hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, dayOfMonth, 1, 0);
        accessComplete.setLeavingDateTime(hourWhenVehicleWentAway);

        String accessLengthOverSomeTime = garage.findAccessLength(accessComplete);
        String expectedTimeLength = dayOfMonth - 1 + "d ";
        assertEquals(expectedTimeLength, accessLengthOverSomeTime);


        int hours = rand.nextInt(23) + 1;
        hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, 1, hours, 0);
        accessComplete.setLeavingDateTime(hourWhenVehicleWentAway);

        String accessLengthOverSomeHours = garage.findAccessLength(accessComplete);
        String expectedHoursLength = hours - 1 + "h ";
        assertEquals(expectedHoursLength, accessLengthOverSomeHours);


        int minutes = rand.nextInt(59) + 1;
        hourWhenVehicleWentAway = LocalDateTime.of(2023, 1, 1, 1, minutes);
        accessComplete.setLeavingDateTime(hourWhenVehicleWentAway);

        String accessLengthOverSomeMinutes = garage.findAccessLength(accessComplete);
        String expectedMinutesLength = minutes + "m ";
        assertEquals(expectedMinutesLength, accessLengthOverSomeMinutes);
    }
}
