package com.i2i.zapcab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "history")
public class History {
    @Id
    private String id;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "pickup", nullable = false, columnDefinition = "VARCHAR(20)")
    private String pickup;
    @Column(name = "drop", nullable = false, columnDefinition = "VARCHAR(20)")
    private String drop;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "distance", nullable = false)
    private int distance;
    @Column(name = "fare", nullable = false)
    private double fare;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="user_id")
    private User user;

    @PrePersist
    protected void onCreate(){
        if(this.id == null){
            this.id = UUID.randomUUID().toString();
        }
    }
}
