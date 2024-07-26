package com.i2i.zapcab.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "vehicles")
public class Vehicle extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "category", columnDefinition = "VARCHAR(5)")
    private String category;
    @Column(name = "type", columnDefinition = "VARCHAR(20)")
    private String type;
    @Column(name = "model", columnDefinition = "VARCHAR(15)")
    private String model;
    @Column(name = "license_plate", columnDefinition = "VARCHAR(25)")
    private String licensePlate;
    @Column(name = "max_seat")
    private int maxSeats;
    @Column(name="status", columnDefinition = "VARCHAR(10)")
    private String status;
}
