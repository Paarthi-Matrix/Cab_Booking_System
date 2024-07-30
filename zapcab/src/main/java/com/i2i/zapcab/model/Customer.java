package com.i2i.zapcab.model;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
 *
 * @see Auditable
 * @see User
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
