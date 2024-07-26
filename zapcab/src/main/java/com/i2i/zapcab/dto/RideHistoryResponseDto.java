package com.i2i.zapcab.dto;

import com.i2i.zapcab.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideHistoryResponseDto {
    private LocalDate date;
    private String pickup;
    private String drop;
    private Date startTime;
    private Date endTime;
    private int distance;
    private double fare;
}
