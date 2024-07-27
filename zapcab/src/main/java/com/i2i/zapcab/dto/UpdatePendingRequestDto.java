package com.i2i.zapcab.dto;

import lombok.*;

/**
 * <p>
 * This class is responsible for managing the details of updating a pending request.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Phone number associated with the pending request. </li>
 *       <li> Status to be updated for the pending request. </li>
 *       <li> Additional remarks or comments about the pending request. </li>
 *   </ol>
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdatePendingRequestDto {
    private String phoneNumber;
    private String status;
    private String remarks;
}
