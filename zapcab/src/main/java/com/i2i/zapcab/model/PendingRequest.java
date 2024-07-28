package com.i2i.zapcab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * <p>
 * The {@code PendingRequest} class represents a pending request entity with various attributes
 * such as name, email, license number, region, city, RC book number, date of birth, mobile number,
 * gender, status, remarks, category, type, model, and license plate.
 * </p>
 *
 * <p>
 * This class extends {@code Auditable}, which means it inherits auditing fields
 * like createdDate and lastModifiedDate.
 * </p>
 * @see Auditable
 * */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pending_requests")
public class PendingRequest extends Auditable {
    @Id
    private String id;
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(20)")
    private String name;
    @Column(name="email", columnDefinition = "VARCHAR(50)")
    private String email;
    @Column(name = "license_no", nullable = false, columnDefinition = "VARCHAR(20)")
    private String licenseNo;
    @Column(name = "region", nullable = false, columnDefinition = "VARCHAR(30)")
    private String region;
    @Column(name = "city", nullable = false, columnDefinition = "VARCHAR(30)")
    private String city;
    @Column(name = "rc_book_no", nullable = false, columnDefinition = "VARCHAR(20)")
    private String rcBookNo;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dob;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "gender", columnDefinition = "VARCHAR(10)")
    private String gender;
    @Column(name = "status", columnDefinition = "VARCHAR(15)")
    private String status;
    @Column(name = "remarks", columnDefinition = "VARCHAR(500)")
    private String remarks;
    @Column(name = "category", columnDefinition = "VARCHAR(10)")
    private String category;
    @Column(name = "type", columnDefinition = "VARCHAR(15)")
    private String type;
    @Column(name = "model", columnDefinition = "VARCHAR(20)")
    private String model;
    @Column(name = "license_plate", columnDefinition = "VARCHAR(20)")
    private String licensePlate;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
