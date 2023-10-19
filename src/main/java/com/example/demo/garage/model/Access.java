package com.example.demo.garage.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "com.example.demo.raz.garage.Access")
@Table(name = "access")
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, name = "parking_date_time")
    private LocalDateTime parkingDateTime;

    @Column(name = "leaving_date_time")
    private LocalDateTime leavingDateTime;

    @Column(name = "amount_to_pay")
    private String amountToPay;

    @Column(name = "parking_spot")
    private Integer parkingSpot;

    @Column(nullable = false, name = "access_complete")
    private Boolean accessComplete;

    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}
