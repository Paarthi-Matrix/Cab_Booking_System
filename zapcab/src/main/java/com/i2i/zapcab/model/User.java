package com.i2i.zapcab.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private String id;
    @Column(name="name", columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;
    @Column(name="email", columnDefinition = "VARCHAR(20)")
    private String email;
    @Column(name = "phone_number", columnDefinition = "LONG")
    private Long phoneNumber;
    @Column(name = "gender", columnDefinition = "VARCHAR(10)")
    private String gender;
    @Column(name="password", columnDefinition = "VARCHAR(20)")
    private String password;
    @Column(name = "date_of_birth",nullable = false)
    private LocalDate dateOfBirth;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Role role;
    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}
