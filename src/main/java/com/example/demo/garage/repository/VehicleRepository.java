package com.example.demo.garage.repository;

import com.example.demo.garage.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Optional<Vehicle> findById(Long id);
    Vehicle getById(Long id);

    //Vehicle deleteById(Long id);

}
