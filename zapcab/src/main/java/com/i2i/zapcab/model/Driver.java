package com.i2i.zapcab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Builder
@Entity
@Data
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "region", nullable = false)
    private String region;
    @Column(name = "no_of_cancellation", nullable = false)
    private int noOfCancellation;
    @Column(name = "license_no", nullable = false, columnDefinition = "VARCHAR(20)")
    private String licenseNo;
    @Column(name = "rc_book_no", nullable = false)
    private String rcBookNo;
    @Column(name = "status", columnDefinition = "VARCHAR(15)")
    private String status;
    @Column(name = "ratings", nullable = false)
    private int ratings;
    @Column(name = "wallet", nullable = false)
    private int wallet;
}
