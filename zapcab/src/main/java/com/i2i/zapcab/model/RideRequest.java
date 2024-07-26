package com.i2i.zapcab.model;

import jakarta.persistence.PrePersist;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "ride_requests")
public class RideRequest extends Auditable {
    @Id
    private String id;
    @Column(name = "pickup_point", columnDefinition = "VARCHAR(15)")
    private String pickupPoint;
    @Column(name = "drop_point", columnDefinition = "VARCHAR(15)")
    private String dropPoint;
    @Column(name = "vehicle_category", columnDefinition = "VARCHAR(15)")
    private String vehicleCategory;
    @Column(name = "drop_time", nullable = false)
    private String dropTime;
    @Column(name = "fare", nullable = false)
    private double fare;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "ride_time", nullable = false)
    private int rideTime;
    @Column(name = "request_status", columnDefinition = "VARCHAR(20)")
    private String status;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @Column(name = "is_deleted", columnDefinition = "Boolean")
    private boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
