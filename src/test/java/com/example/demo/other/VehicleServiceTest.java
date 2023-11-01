package com.example.demo.other;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.VehicleRepository;
import com.example.demo.garage.service.AccessService;
import com.example.demo.garage.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    AccessService accessService;

    @InjectMocks
    VehicleService vehicleService;


    @Test
    void testFindVehicleByLicensePlate() {

        Vehicle vehicle = Vehicle.builder()
                                .model("Mustang")
                                .licensePlate("ARG-OOP-202")
                                .unregistered(false)
                                .registrationDate(LocalDate.now())
                                .build();

        Optional<Vehicle> empty = Optional.empty();
        when(vehicleRepository.findByLicensePlate("XXXXXXX")).thenReturn(empty);
        Vehicle vehicleNotFounded = vehicleService.findVehicleByLicensePlate("XXXXXXX");
        assertNull(vehicleNotFounded);

        Optional<Vehicle> vehicleToFind = Optional.of(vehicle);
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(vehicleToFind);
        Vehicle vehicleFounded = vehicleService.findVehicleByLicensePlate("ARG-OOP-202");
        assertNotNull(vehicleFounded);
    }


    @Test
    void testRegisterVehicle() {

        Vehicle vehicle = Vehicle.builder()
                                .model("Mustang")
                                .licensePlate("ARG-OOP-202")
                                .unregistered(false)
                                .registrationDate(LocalDate.now())
                                .build();

        Optional<Vehicle> optionalVehicle = Optional.of(vehicle);
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicle);
        Vehicle vehicleAlreadyRegistered = vehicleService.registerVehicle(vehicle);
        assertNull(vehicleAlreadyRegistered);

        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(Optional.empty());
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle vehicleToRegister = vehicleService.registerVehicle(vehicle);
        assertFalse(vehicleToRegister.getUnregistered());
    }


    @Test
    void testReRegisterVehicle() {

        Vehicle vehicle = Vehicle.builder()
                                .model("Mustang")
                                .licensePlate("ARG-OOP-202")
                                .unregistered(false)
                                .registrationDate(LocalDate.now())
                                .build();

        Optional<Vehicle> optionalVehicle = Optional.empty();
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicle);
        Vehicle vehicleNotFound = vehicleService.reRegisterVehicle("ARG-OOP-202");
        assertNull(vehicleNotFound);

        Optional<Vehicle> optionalVehicleSecondCase = Optional.of(vehicle);
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicleSecondCase);
        Vehicle vehicleAlreadyRegistered = vehicleService.reRegisterVehicle("ARG-OOP-202");
        assertNull(vehicleAlreadyRegistered);

        vehicle.setUnregistered(true);
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicleSecondCase);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        Vehicle vehicleReRegistered = vehicleService.reRegisterVehicle("ARG-OOP-202");
        assertFalse(vehicleReRegistered.getUnregistered());
    }


    @Test
    void testRegisterVehicleAndPark() {

        Vehicle vehicle = Vehicle.builder()
                                .model("Mustang")
                                .licensePlate("ARG-OOP-202")
                                .unregistered(false)
                                .registrationDate(LocalDate.now())
                                .build();

        Optional<Vehicle> optionalVehicle = Optional.of(vehicle);
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicle);
        Access vehicleAlreadyRegistered = vehicleService.registerVehicleAndPark(vehicle, 6);
        assertNull(vehicleAlreadyRegistered);

        vehicle.setUnregistered(true);
        Optional<Vehicle> optionalVehicleSecondCase = Optional.empty();
        when(vehicleRepository.findByLicensePlate("ARG-OOP-202")).thenReturn(optionalVehicleSecondCase);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(accessService.createNewAccessIn(vehicle.getId(), 6)).thenReturn(new Access());
        Access newAccess = vehicleService.registerVehicleAndPark(vehicle, 6);
        assertFalse(vehicle.getUnregistered());
        assertNotNull(newAccess);
    }


    @Test
    void testUnregisterVehicle() {

        Vehicle vehicle = Vehicle.builder()
                                .id(1L)
                                .model("Mustang")
                                .licensePlate("ARG-OOP-202")
                                .unregistered(false)
                                .registrationDate(LocalDate.now())
                                .accesses(new ArrayList<>())
                                .build();

        Optional<Vehicle> optionalVehicle = Optional.empty();
        when(vehicleRepository.findById(1L)).thenReturn(optionalVehicle);
        Vehicle vehicleNotFound = vehicleService.unregisterVehicle(1L);
        assertNull(vehicleNotFound);

        vehicle.setUnregistered(true);
        Optional<Vehicle> optionalVehicleSecondCase = Optional.of(vehicle);
        when(vehicleRepository.findById(1L)).thenReturn(optionalVehicleSecondCase);
        Vehicle vehicleAlreadyUnregistered = vehicleService.unregisterVehicle(1L);
        assertNull(vehicleAlreadyUnregistered);

        vehicle.setUnregistered(false);
        Access incompleteAccess = Access.builder()
                                        .parkingDateTime(null)
                                        .vehicle(vehicle)
                                        .parkingSpot(6)
                                        .accessComplete(false)
                                        .build();
        List<Access> accesses = List.of(incompleteAccess);
        vehicle.setAccesses(accesses);
        Optional<Vehicle> optionalVehicleThirdCase = Optional.of(vehicle);
        when(vehicleRepository.findById(1L)).thenReturn(optionalVehicleThirdCase);
        Vehicle vehicleStillParked = vehicleService.unregisterVehicle(1L);
        assertNull(vehicleStillParked);

        incompleteAccess.setAccessComplete(true);
        List<Access> accessComplete = List.of(incompleteAccess);
        vehicle.setAccesses(accessComplete);
        Optional<Vehicle> optionalVehicleFourthCase = Optional.of(vehicle);
        when(vehicleRepository.findById(1L)).thenReturn(optionalVehicleFourthCase);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        Vehicle vehicleToUnregister = vehicleService.unregisterVehicle(1L);
        assertEquals(vehicleToUnregister, vehicle);
    }
}
