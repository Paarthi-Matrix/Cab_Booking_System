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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.*;

import java.util.UUID;

import java.util.Date;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rides")
public class Ride extends Auditable {
    @Id
    private String id;
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(15)")
    private String status;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "fare", nullable = false)
    private double fare;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
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
    @Column(name = "payment_mode", columnDefinition = "VARCHAR(20)")
    private String paymentMode;
    @Column(name = "isDeleted")
    private boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
