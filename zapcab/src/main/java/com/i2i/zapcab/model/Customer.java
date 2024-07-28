package com.i2i.zapcab.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * <p>
 * This class represents a customer entity with a unique identifier,
 * tier information, and an associated user.
 * </p>
 *
 * <p>
 * This class extends {@code Auditable}, which means it inherits auditing fields
 * like createdDate and lastModifiedDate.
 * </p>
 *  @see Auditable
 *  @see User
 */
 @Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer extends Auditable {
    @Id
    private String id;
    @Column(name = "tier", nullable = false)
    private String tier;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
