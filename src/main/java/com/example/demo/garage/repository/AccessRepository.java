package com.example.demo.garage.repository;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRepository extends JpaRepository<Access, Long> {

    //@Query("SELECT a FROM Access a WHERE a.vehicle.license_plate = 'licensePlate'")
    List<Access> findByAccessComplete(Boolean accessComplete);

    List<Access> findByVehicle(Vehicle vehicle);

}
