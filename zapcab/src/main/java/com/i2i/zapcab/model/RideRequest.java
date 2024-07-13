package com.i2i.zapcab.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@Table(name = "ride_request")
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "pickup_point", columnDefinition = "VARCHAR(15)")
    private String pickupPoint;
    @Column(name = "drop_point", columnDefinition = "VARCHAR(15)")
    private String dropPoint;
    @Column(name = "vehicle_category", columnDefinition = "VARCHAR(15)")
    private String vehicleCategory;
    @Column(name = "drop_time", nullable = false)
    private Date dropTime;
    @Column(name = "fare", nullable = false)
    private int fare;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "ride_time", nullable = false)
    private Date rideTime;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "customer_id")
    private Set<Customer> customers;
}
