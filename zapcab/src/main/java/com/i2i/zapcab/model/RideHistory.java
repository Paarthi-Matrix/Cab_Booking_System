package com.i2i.zapcab.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ride_history")
public class RideHistory {
    @Id
    private String id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "pickup_point", nullable = false, columnDefinition = "VARCHAR(20)")
    private String pickupPoint;
    @Column(name = "drop_point", nullable = false, columnDefinition = "VARCHAR(20)")
    private String dropPoint;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "fare", nullable = false)
    private double fare;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
