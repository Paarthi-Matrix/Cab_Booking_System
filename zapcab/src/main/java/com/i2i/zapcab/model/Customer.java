package com.i2i.zapcab.model;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Data;

@Builder
@Entity
@Data
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tier", nullable = false)
    private String tier;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
