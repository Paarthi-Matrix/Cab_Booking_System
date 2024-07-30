package com.i2i.zapcab.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.i2i.zapcab.helper.RoleEnum;

@NoArgsConstructor
@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleEnum roleName;

    @Builder
    public Role(RoleEnum roleName) {
        this.roleName = roleName;
    }
}
