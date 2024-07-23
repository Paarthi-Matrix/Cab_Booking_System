package com.i2i.zapcab.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The SuccessMessage class represents a structured success message that can be used
 * for returning success responses in a standardized format. This class includes attributes
 * such as a message, additional data, and a status code. It is designed to be used with
 * frameworks like Spring Boot for providing consistent success responses in RESTful APIs.
 *
 * <p>Attributes:
 * <ul>
 *     <li>{@code message} - A user-friendly message describing the success.</li>
 *     <li>{@code data} - Additional data related to the success (generic type).</li>
 *     <li>{@code status} - HTTP status code representing the success.</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>{@code
 * // Creating a success message with a message, data, and status
 * SuccessMessage<String> successMessage = new SuccessMessage<>("Operation successful", "Additional success data", HttpStatus.OK.value());
 * }</pre>
 *
 * @param <T> The type of the additional data related to the success.
 */
@Builder
@NoArgsConstructor
@Getter
@Setter
public class SuccessMessage<T> {
        private String message;
        private T data;
        private int status;

        public SuccessMessage(String message, T data, int status) {
            this.message = message;
            this.data = data;
            this.status = status;
        }
        public String getMessage() {
            return message;
        }
        public T getData() {
            return data;
        }
        public int getStatus() {
            return status;
        }
}
