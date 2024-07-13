package com.i2i.zapcab.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@Table(name = "pending_requests")
public class PendingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(20)")
    private String name;
    @Column(name = "license_no", nullable = false, columnDefinition = "VARCHAR(20)")
    private String licenseNo;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "rc_book_no", nullable = false)
    private String rcBookNo;
    @Column(name = "date_of_birth",nullable = false)
    private LocalDate dob;
    @Column(name = "phone_number", columnDefinition = "LONG")
    private Long phoneNumber;
    @Column(name = "gender", columnDefinition = "VARCHAR(10)")
    private String gender;
    @Column(name = "status", columnDefinition = "VARCHAR(15)")
    private String status;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "user_id")
    private User user;
}
