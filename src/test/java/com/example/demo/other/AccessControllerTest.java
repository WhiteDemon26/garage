package com.example.demo.other;

import com.example.demo.garage.controller.AccessController;
import com.example.demo.garage.model.Access;
import com.example.demo.garage.service.AccessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessControllerTest {

    @Mock
    AccessService accessService;

    @InjectMocks
    AccessController accessController;


    @Test
    void testFindAccess() {

        Access completeAccess = Access.builder()
                                        .id(1L)
                                        .parkingDateTime(null)
                                        .parkingSpot(5)
                                        .accessComplete(true)
                                        .build();

        when(accessService.findAccess(1L)).thenReturn(completeAccess);
        ResponseEntity<Access> responseEntity = accessController.findAccess(1L);

        Access foundAccess = responseEntity.getBody();
        assertNotNull(foundAccess);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testAddParkedVehicle() {

        when(accessService.createNewAccessIn(1L, 6)).thenReturn(new Access());
        ResponseEntity<Access> responseEntity = accessController.addParkedVehicle(1L, 6);

        Access foundAccess = responseEntity.getBody();
        assertNotNull(foundAccess);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testRemoveParkedVehicle() {

        when(accessService.completeAccessOut(1L)).thenReturn(new Access());
        ResponseEntity<Access> responseEntity = accessController.removeParkedVehicle(1L);

        Access vehicleRemoved = responseEntity.getBody();
        assertNotNull(vehicleRemoved);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }


    @Test
    void testShowTimesParkedByLicensePlate() {

        when(accessService.findAccessesByLicensePlates("ARG-OOP-202")).thenReturn(new ArrayList<>());
        ResponseEntity<List<Access>> responseEntity = accessController.showTimesParkedByLicensePlate("ARG-OOP-202");

        List<Access> vehicleParkedTimes = responseEntity.getBody();
        assertNotNull(vehicleParkedTimes);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
