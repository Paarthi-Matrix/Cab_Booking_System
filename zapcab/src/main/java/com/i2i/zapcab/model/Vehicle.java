package com.i2i.zapcab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@Setter
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
}
