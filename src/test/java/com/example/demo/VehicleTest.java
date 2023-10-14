package com.example.demo;

import com.example.demo.garage.model.Vehicle;
import com.example.demo.garage.model.vehicles.Car;
import com.example.demo.garage.model.vehicles.Motorbike;
import com.example.demo.garage.model.vehicles.Van;
import com.example.demo.garage.service.AccessService;

public class VehicleTest {






    // include an index counting the number of free spots in the garage, changing as vehicles get parked and removed
    // include a warning message when the parking lot is full: NO MORE VEHICLES PLEASE; THE PARKING LOT IS FULL!!

    // gestire la possibilità o meno di parcheggiare a seconda dell' orario
    // stabilisci un intervallo di ore, per esempio dalle 8 alle 23, e consenti l'uso del parcheggio solo in questo range di tempo
    // quindi LT.now() (cioè quando un utente prova a parcheggiare) deve essere compreso in questo range, altrimenti deve solo ottenre un messaggio di errore

    // rename the assicurazione() method (choose an English name) and use English in all its overrides

    // create for the garage project and related classes a specific package
    // move the Employee and other classes, together with the inheritance package, to the tutorial package

    // add vehicle license plate

    // allow vehicles to park at different times of the day
    // hint: for now, the only time you consider is now(): change that with an hour decided by the customer

    // manage the parking and leaving times together with a payment system. The longer you stay, the more you pay.
    // Communicate to the driver the amount of money he has to pay when he leaves.
    // hint1: edit the parkingSpotFree() method  so that, when a vehicle leaves, the driver must pay in proportion to the number of minutes his car was parked
    // hint2: at least another method is necessary...

    // include dates in your functionality: parking and leaving times need to become parking and leaving dates & times.

    // add leaving date & time information to each vehicle

    // use this code to make a garage app, managing the traffic in and out of the garage, recording when vehicles come and go and how much their owners paid.
    //  Every time a vehicle enters the garage, a new record for that vehicle must be created in the DB. When the vehicle leaves, that record needs to be updated.

    // rename MotorVehicle -> Vehicle

    // create a new class representing an access to the garage. Its field should be all the info currently included in MotorVehicle but that have nothing to do
    //  with the type of car parked (that is parkingDateTime, etc.).
    //  This class should be an entity of its own. Figure out what type of relation (OTM, MTO, MTM) it should have with MotorVehicle and refactor the code accordingly

    // add a few columns in Vehicle and use English names

    // remove errors below

    // complete the methods below by integrating access creation & update functionalities when someone uses the garage.

    // create appropriate packages for your classes, they shouldn't be all together in the same one

    // add the system functionality to view an access by id

    // change VehicleService: remove the errors and all the logic about using the garage, leaves only the stuff concerning finding a vehicle by license plate
    //  and add the possibility to register a vehicle (which will be stored in the DB)

    // refactor AccessService: it needs to have all the functionality required to users

    // remove all logs from controllers, move them to the services. If they mark the completion of the operation, pick the right positions for them
    //  (in other words, don't show them when errors occur)

    // add the following functionality: once a car is parked, you cannot park the same car
    //  hint: use the accessComplete field to check if the user has already parked his car but hasn't left yet.

    // change the remove vehicle functionality: use the id of the vehicle, not that of the access

    // add the following functionality: you cannot remove a vehicle that has not been already parked

    // change the remove vehicle functionality: remove the pointless parts

    // add the following functionality: yoo can register yur vehicle the first time you try to access the garage too

    // update the state of the access before allowing a vehicle to leave the garage

    // remove the following bug: empty vehicles list despite records access being present in the DB
    //  hint: what's the difference between ddl-auto: none and ddl-auto: create?




    // TODO: add the unregister vehicle functionality. Crate an 'unregistered vehicle' record in the vehicle table.
    //  When an user unregisters his vehicle, all his previous accesses must be associated with this fake register
    //  in order to prevent Hibernate from deleting them.


    // TODO: remove the following inefficiency: when a vehicle gets unregistered, don't update the accesses in a loop one by one, save them all in one shot


    // TODO: add the following functionality: you cannot unregister a vehicle if there still an incomplete access associated with your vehicle


    // TODO: remove the following inefficiency: don't query the DB for the fake vehicle every time someone wants to unregister his vehicle
    //  (hint: save it somewhere once and for all when it is necessary)


    // TODO: create an 'unregistered' column in the vehicle table to indicate if the vehicle has been unsubscribed.
    //  Comments all the code involving the fake vehicle and explain why it's now useless.


    // TODO: improves the log messages, add '!' and info about ids of access and vehicles retrieved.
    //  Check if \n works.


    // TODO: correct the log "You parked your Long at: 2023-10-11T10:54:47.438873200. Have a good day!"


    // TODO: add the registration date to the vehicle table and related functionality app-side


    // TODO: remove the pdt_string_format column from the access table


    // TODO: remove from AccessService any info about the state of the garage, put it in a new class specific for that.



    // make licensePlate the id of Vehicle


    // add a field in the Access class indicating if the access is complete or not. Integrate it with the rest of the app.


    // est all the APIs and verify everything works; register other vehicles, park them and remove some of them.


    // change your payment system in case someone leaves his vehicle in the parking lot for a whole night.
    // set a fixed amount of money to pay for the night, not the minute-based one










    AccessService accessService;

    public void newMain() {

        Car shelbyMustang = Car.builder()
                .model("Ford")
                .year(2006)
                .cubeCapacity(5200)
                .porte(2)
                .alimentazione("Benzina")
                .electric(true)
                //.parkingDateTime(LocalDateTime.of(2023, 8, 21, 4, 35))
                .build();

        Motorbike ninjaH2 = Motorbike.builder()
                .model("Kawasaki")
                .year(2015)
                .cubeCapacity(998)
                .tempi(4)
                .electric(true)
                //.parkingDateTime(LocalDateTime.of(2023, 8, 20, 13, 10))
                .build();

        Van transitVan = Van.builder()
                .model("Ford")
                .year(2012)
                .cubeCapacity(2000)
                .capacity("2,4 tonnellate")
                .electric(false)
                //.parkingDateTime(LocalDateTime.of(2023, 8, 22, 12, 55))
                .build();

        Car panda = Car.builder()
                .model("Fiat")
                .year(2006)
                .cubeCapacity(1000)
                .porte(4)
                .alimentazione("Benzina")
                .electric(false)
                //.parkingDateTime(LocalDateTime.of(2023, 8, 22, 12, 20))
                .build();

        accessService.parkVehicleIfPossible(5L, 0);
        accessService.parkVehicleIfPossible(5L, 1);
        accessService.parkVehicleIfPossible(5L, 2);
        accessService.parkVehicleIfPossible(5L,3);


        System.out.println("");
        System.out.println("time goes by...");
        System.out.println("time goes by...");
        System.out.println("");


    }

    //just fr testing
    public void situationParking() {

        Vehicle[] vehicles = new Vehicle[15];

        for (int i = 0; i < vehicles.length; i++) {
            if(vehicles[i] == null) {
                System.out.println("spot " + i + ": empty");
            } else {
                System.out.println("spot " + i + ": " + vehicles[i].getClass().getName());
            }
        }
    }
}
