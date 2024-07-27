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

/**
 * <p>
 * This class is responsible for managing the details of a ride history response.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Date when the ride took place. </li>
 *       <li> Pickup point location for the ride. </li>
 *       <li> Drop point location for the ride. </li>
 *       <li> Start time of the ride. </li>
 *       <li> End time of the ride. </li>
 *       <li> Distance covered during the ride in kilometers or miles. </li>
 *       <li> Fare charged for the ride. </li>
 *   </ol>
 * </p>
 */
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
