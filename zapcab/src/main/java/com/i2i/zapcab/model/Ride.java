package com.i2i.zapcab.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Builder
@Getter
@Setter
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
    @Column(name = "start_time", nullable = false)
    private Date startTime;
    @Column(name = "end_time", nullable = false)
    private Date endTime;
    @Column(name = "ride_rating", nullable = false)
    private float rideRating;
    @Column(name = "drop_point", columnDefinition = "VARCHAR(20)", nullable = false)
    private String dropPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private RideRequest rideRequest;
}
