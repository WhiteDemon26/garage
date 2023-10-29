package com.example.demo.garage.service;

import com.example.demo.garage.model.Access;
import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.repository.AccessRepository;
import com.example.demo.garage.repository.VehicleRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class VehicleService {

    public final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    //private Vehicle fakeVehicle;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private AccessService accessService;


    // NOT TO USE
    //@PostConstruct
    private void oldPostConstruct() {
        if(!vehicleRepository.findByLicensePlate("XXXXXXX").isPresent()) {
            Vehicle fakeVehicle = Vehicle.builder()
                    .model("fakeVehicle")
                    .licensePlate("XXXXXXX")
                    .build();
            vehicleRepository.save(fakeVehicle);
            System.out.println("\n Fake record created !! \n");
        }
    }


    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        Optional<Vehicle> vehicle = this.vehicleRepository.findByLicensePlate(licensePlate);
        if(!vehicle.isPresent()) {
            System.out.println("\n A vehicle with the license plate " + licensePlate + " is not present! \n");
            return null;
        }
        String message = "\n You found a vehicle (see this response's body) !! \n";
        System.out.println(message);
        return vehicle.get();
    }


    public Vehicle registerVehicle(Vehicle vehicle) {
        if(!vehicle.getLicensePlate().isEmpty()) {
            System.out.println("\n A vehicle with the license plate " + vehicle.getLicensePlate() + " is already registered! \n");
            return null;
        }
        LocalDate registrationDate = LocalDate.now();
        vehicle.setRegistrationDate(registrationDate);
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("\n The user has been correctly registered (see this response's body) !! \n");
        return vehicle;
    }


    public Vehicle reRegisterVehicle(String licensePlate) {

        Optional<Vehicle> vehicleFound = this.vehicleRepository.findByLicensePlate(licensePlate);
        if(!vehicleFound.isPresent()) {
            System.out.println("\n A vehicle with the license plate " + licensePlate + " is not present! \n");
            return null;
        }
        Vehicle vehicle = vehicleFound.get();
        if(!vehicle.getUnregistered()) {
            System.out.println("\n The vehicle (id: " + vehicle.getId() + ") is already registered, you cannot register it again! \n");
            return null;
        }
        vehicle.setUnregistered(false);
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("\n Your vehicle has been correctly re-registered (see this response's body) !! \n");
        return vehicle;
    }


    public Access registerVehicleAndPark(Vehicle vehicle, Integer parkingSpot) {
        vehicle.setRegistrationDate(LocalDate.now());
        vehicle.setUnregistered(false);
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("\n The vehicle has been correctly registered !! \n");
        return accessService.createNewAccessIn(vehicle.getId(), parkingSpot);
    }


    public Vehicle unregisterVehicle(Long vehicleId) {

        Optional<Vehicle> vehicleToUnregister = this.vehicleRepository.findById(vehicleId);
        if(!vehicleToUnregister.isPresent()) {
            System.out.println("\n A vehicle with the id" + vehicleId + " is not present! \n");
            return null;
        }

        Vehicle vehicle = vehicleToUnregister.get();
        List<Access> accesses = vehicle.getAccesses();
        Boolean vehicleUnregistered = vehicle.getUnregistered();

        if(vehicleUnregistered) {
            System.out.println("\n The vehicle (id: " + vehicleId + ") is just unregistered, you cannot unregister it again. \n");
            return null;
        }

        for(Access access : accesses) {
            if(!access.getAccessComplete()) {
                System.out.println("\n The vehicle (id: " + vehicleId + ") is still parked, you cannot unregister it. \n");
                return null;
            }
        }
        vehicle.setUnregistered(true);
        vehicle = vehicleRepository.save(vehicle);
        System.out.println("\n The vehicle has been correctly unregistered (see this response's body) !! \n");
        return vehicle;
    }


    // NOT TO USE
    public Vehicle unregisterVehicleOldVersion(Long vehicleId) {

        Vehicle vehicleToUnregister = vehicleRepository.findById(vehicleId).get();
        List<Access> accesses = vehicleToUnregister.getAccesses();

        /*
        if(this.fakeVehicle == null) {
            this.fakeVehicle = this.vehicleRepository.findByLicensePlate("XXXXXXX").get();
        }

        for(Access access : accesses) {
            if(!access.getAccessComplete()) {
                System.out.println("\n This vehicle is still parked, you cannot unregister it. \n");
                return null;
            }
            access.setVehicle(this.fakeVehicle);
        }
        */
        accessRepository.saveAll(accesses);

        System.out.println("\n The user has been correctly unregistered (see this response's body) !! \n");
        return vehicleToUnregister;
    }
}
