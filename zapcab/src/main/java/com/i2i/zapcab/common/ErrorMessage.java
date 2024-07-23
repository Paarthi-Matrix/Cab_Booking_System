package com.i2i.zapcab.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * The ErrorMessage class represents a structured error message that can be used
 * for returning error responses in a standardized format. This class provides various
 * constructors to create error messages with different levels of detail, including
 * status codes, timestamps, messages, debug messages, and additional data.
 *
 * <p>Attributes:
 * <ul>
 *     <li>{@code status} - HTTP status code representing the error.</li>
 *     <li>{@code timestamp} - Timestamp indicating when the error occurred, formatted as "dd-MM-yyyy hh:mm:ss".</li>
 *     <li>{@code message} - A user-friendly message describing the error.</li>
 *     <li>{@code debugMessage} - A detailed message useful for debugging purposes.</li>
 *     <li>{@code e} - The exception that caused the error (must not be null).</li>
 *     <li>{@code data} - Additional data related to the error (generic type).</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>{@code
 * // Creating an error message with a status and exception
 * ErrorMessage<String> errorMessage = new ErrorMessage<>(HttpStatus.BAD_REQUEST.value(), new RuntimeException("Invalid input"));
 *
 * // Creating an error message with a status, message, and exception
 * ErrorMessage<String> detailedErrorMessage = new ErrorMessage<>(HttpStatus.NOT_FOUND.value(), "Resource not found", new UnexpectedException());
 *
 * // Creating an error message with a message, data, status, and exception
 * ErrorMessage<String> fullErrorMessage = new ErrorMessage<>("Operation failed", "Additional error data", HttpStatus.INTERNAL_SERVER_ERROR.value(), new InternalServerErrorException());
 * }</pre>
 *
 * <p>This class is designed to work seamlessly with frameworks like Spring Boot for providing
 * consistent error responses in RESTful APIs.
 *
 * @param <T> The type of the additional data related to the error.
 */
@Getter
@Setter
public class ErrorMessage<T> {
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    @NotNull
    private Exception e;
    private T data;

    private ErrorMessage() {
        timestamp = LocalDateTime.now();
    }

    ErrorMessage(int status) {
        this();
        this.status = status;
    }

    ErrorMessage(int status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ErrorMessage(int status, String message, Throwable e) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = e.getLocalizedMessage();
    }

    public ErrorMessage(String message, T data, int status, Exception e) {
        this();
        this.status = status;
        this.message = e.getMessage();
        this.e = e;
    }
}

