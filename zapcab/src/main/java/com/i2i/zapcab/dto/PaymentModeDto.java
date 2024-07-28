package com.i2i.zapcab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * This class is responsible for managing the payment mode details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> Payment mode used for the transaction (e.g., credit card, debit card, cash). </li>
 *   </ol>
 * </p>
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentModeDto {
    private String paymentMode;
}
