package com.i2i.zapcab.model;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
