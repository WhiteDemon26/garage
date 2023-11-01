package com.example.demo.other;

import com.example.demo.garage.controller.VehicleController;
import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleControllerTest {

    @Mock
    VehicleService vehicleService;

    @InjectMocks
    VehicleController vehicleController;


    @Test
    void testFindVehicleByLicensePlate() {

        Vehicle vehicleToFind = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();

        when(vehicleService.findVehicleByLicensePlate("ARG-OOP-202")).thenReturn(vehicleToFind);
        ResponseEntity<Vehicle> responseEntity = vehicleController.findVehicleByLicensePlate("ARG-OOP-202");

        Vehicle foundVehicle = responseEntity.getBody();
        assertNotNull(foundVehicle);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testRegisterVehicle() {

        Vehicle vehicleToRegisterFirstTime = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();

        when(vehicleService.registerVehicle(vehicleToRegisterFirstTime)).thenReturn(vehicleToRegisterFirstTime);
        ResponseEntity<Vehicle> responseEntity = vehicleController.registerVehicle(vehicleToRegisterFirstTime);

        Vehicle foundVehicle = responseEntity.getBody();
        assertNotNull(foundVehicle);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testRegisterVehicleAndPark() {

        Vehicle vehicleToRegisterAndPark = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();

        when(vehicleService.registerVehicleAndPark(vehicleToRegisterAndPark, 5)).thenReturn(new Access());
        ResponseEntity<Access> responseEntity = vehicleController.registerVehicleAndPark(vehicleToRegisterAndPark, 5);

        Access foundAccess = responseEntity.getBody();
        assertNotNull(foundAccess);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testReRegisterVehicle() {

        Vehicle vehicleToRegisterSecondTime = Vehicle.builder()
                .model("Mustang")
                .licensePlate("ARG-OOP-202")
                .unregistered(false)
                .registrationDate(LocalDate.now())
                .build();

        when(vehicleService.reRegisterVehicle("ARG-OOP-202")).thenReturn(vehicleToRegisterSecondTime);
        ResponseEntity<Vehicle> responseEntity = vehicleController.reRegisterVehicle("ARG-OOP-202");

        Vehicle foundVehicle = responseEntity.getBody();
        assertNotNull(foundVehicle);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testUnregisterVehicle() {

        Vehicle unregisteredVehicle = Vehicle.builder()
                                                .id(1L)
                                                .model("Mustang")
                                                .licensePlate("ARG-OOP-202")
                                                .unregistered(true)
                                                .registrationDate(LocalDate.now())
                                                .build();

        when(vehicleService.unregisterVehicle(1L)).thenReturn(unregisteredVehicle);
        ResponseEntity<Vehicle> responseEntity = vehicleController.unregisterVehicle(1L);

        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().getUnregistered());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
