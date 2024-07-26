package com.i2i.zapcab.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * <p>
 *     The Auditable class is a base class for all entities that require auditing functionality.
 *     This class leverages Spring Data JPA's auditing features to automatically populate the audit
 *     fields using the {@link AuditingEntityListener}.
 * </p>
 * <p>
 *     It includes the following audit fields:
 * </p>
 * <ul>
 *     <li>createdBy: The user who created the entity.</li>
 *     <li>createdDate: The date and time when the entity was created.</li>
 *     <li>lastModifiedBy: The user who last modified the entity.</li>
 *     <li>lastModifiedDate: The date and time when the entity was last modified.</li>
 * </ul>
 * <p>
 *     These fields are automatically populated by Spring Data JPA, and this class should be extended
 *     by entities that need to be audited.
 * </p>
 */

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
