package com.i2i.zapcab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Builder
@Entity
@Data
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "category", columnDefinition = "VARCHAR(5)")
    private String category;
    @Column(name = "type", columnDefinition = "VARCHAR(10)")
    private String type;
    @Column(name = "model", columnDefinition = "VARCHAR(15)")
    private String model;
    @Column(name = "license_plate", columnDefinition = "VARCHAR(12)")
    private String licensePlate;
    @Column(name = "max_seat")
    private int maxSeats;
    @Column(name="status", columnDefinition = "VARCHAR(10)")
    private String status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private Driver driver;
}
