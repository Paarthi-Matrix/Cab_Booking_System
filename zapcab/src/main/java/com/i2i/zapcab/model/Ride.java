package com.i2i.zapcab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Builder
@Data
@Entity
@Table(name = "rides")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(15)")
    private String status;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "fare", nullable = false)
    private int fare;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "ride_rating")
    private float rideRating;
    @Column(name = "drop_point", columnDefinition = "VARCHAR(20)")
    private String dropPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private RideRequest rideRequest;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
