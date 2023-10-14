package com.example.demo.garage.service;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class VehicleService {

    public final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AccessRepository accessRepository;

    @PostConstruct
    private void postConstruct() {
        if(!vehicleRepository.findByLicensePlate("XXXXXXX").isPresent()) {
            Vehicle fakeVehicle = Vehicle.builder()
                    .model("fakeVehicle")
                    .licensePlate("XXXXXXX")
                    .build();
            vehicleRepository.save(fakeVehicle);
            System.out.println("\nFake record created !!\n");
        }
    }


    public Optional<Vehicle> findVehicleByLicensePlate(String licensePlate) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        String message = "You found a vehicle (see this response's body) !!";
        System.out.println(message);
        return vehicle;
    }


    public Vehicle registerVehicle(Vehicle vehicle) {
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("The user has been correctly registered (see this response's body) !!");
        return vehicle;
    }


    public Vehicle unregisterVehicle(Long vehicleId) {

        Vehicle fakeVehicle = vehicleRepository.findByLicensePlate("XXXXXXX").get();
        Vehicle vehicleToUnregister = vehicleRepository.findById(vehicleId).get();
        List<Access> accesses = vehicleToUnregister.getAccesses();

        for(Access access : accesses) {
            access.setVehicle(fakeVehicle);
            accessRepository.save(access);
        }

        System.out.println("The user has been correctly unregistered (see this response's body) !!");
        return vehicleToUnregister;
    }
}
