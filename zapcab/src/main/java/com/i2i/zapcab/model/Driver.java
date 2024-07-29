package com.i2i.zapcab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * This class represents a driver entity with various attributes
 * such as region, number of cancellations, license number, RC book number, status,
 * ratings, and wallet balance. Each driver is associated with a user and a vehicle.
 * </p>
 *
 * <p>
 * This class extends {@code Auditable}, which means it inherits auditing fields
 * like createdDate and lastModifiedDate.
 * </p>
 *  @see Auditable
 *  @see User
 *  @see Vehicle
 */
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "drivers")
public class Driver extends Auditable {
    @Id
    private String id;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Vehicle vehicle;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "driver")
    private VehicleLocation vehicleLocation;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
