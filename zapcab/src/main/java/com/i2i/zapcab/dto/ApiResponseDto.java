package com.i2i.zapcab.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.i2i.zapcab.common.ErrorMessage;
import com.i2i.zapcab.common.SuccessMessage;

/**
 * <p>
 * The ApiResponseDto class is a generic DTO (Data Transfer Object) used for wrapping responses
 * in a consistent format across the application. It extends the ResponseEntity class from Spring
 * to provide HTTP status codes along with the response data and messages.
 * </p>
 *
 * <p>
 * The class includes a timestamp to indicate when the response was created, a message providing
 * information about the response, and the actual data being returned. There are two constructors
 * for success and error responses, encapsulating the appropriate messages and status codes.
 * </p>
 *
 * <p>
 * The class provides several static methods for creating ApiResponseDto instances with various HTTP
 * status codes, including OK, CREATED, ACCEPTED, NO_CONTENT, UNAUTHORIZED, INTERNAL_SERVER_ERROR, and
 * BAD_REQUEST. Each method returns an ApiResponseDto with the appropriate status code, message, and data.
 * </p>
 *
 * <p>
 * This class ensures that all API responses have a consistent structure, which improves the readability
 * and maintainability of the code, as well as the ease of understanding for API consumers.
 * </p>
 *
 * @param <T> The type of the data being returned in the response.
 */
@Getter
@Setter
public class ApiResponseDto<T> extends ResponseEntity<Object> {
    private LocalDateTime timestamp;
    private String message;
    private T data;

    private ApiResponseDto(SuccessMessage<T> successMessage, HttpStatus httpStatus) {
        super(successMessage, httpStatus);
        this.timestamp = LocalDateTime.now();
        this.message = successMessage.getMessage();
        this.data = successMessage.getData();
    }

    private ApiResponseDto(ErrorMessage<T> errorMessage, HttpStatus httpStatus) {
        super(errorMessage, httpStatus);
        this.timestamp = LocalDateTime.now();
        this.message = errorMessage.getMessage();
        this.data = errorMessage.getData();
    }

    public static <T> ApiResponseDto<T> statusOk(T data) {
        return new ApiResponseDto<>(new SuccessMessage<>(
                "Operation successful",
                data,
                HttpStatus.OK.value()), HttpStatus.OK);
    }

    public static <T> ApiResponseDto<T> statusCreated(T data) {
        return new ApiResponseDto<>(new SuccessMessage<>(
                "Resource created successfully",
                data,
                HttpStatus.CREATED.value()), HttpStatus.CREATED);
    }

    public static <T> ApiResponseDto<T> statusAccepted(T data) {
        return new ApiResponseDto<>(new SuccessMessage<>(
                "Request accepted",
                data,
                HttpStatus.ACCEPTED.value()), HttpStatus.ACCEPTED);
    }

    public static <T> ApiResponseDto<T> statusNoContent(T data) {
        return new ApiResponseDto<>(new SuccessMessage<>(
                "No content available",
                data,
                HttpStatus.NO_CONTENT.value()
        ), HttpStatus.NO_CONTENT);
    }

    public static <T> ApiResponseDto<T> statusUnAuthorized(T data, Exception e) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                "User unauthorized",
                data,
                HttpStatus.UNAUTHORIZED.value(),
                e), HttpStatus.UNAUTHORIZED);
    }

    public static <T> ApiResponseDto<T> statusInternalServerError(T data, Exception e) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                "Un expected error occurred",
                data,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponseDto<T> statusBadRequest(T data, Exception e) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                "Invalid request",
                data,
                HttpStatus.BAD_REQUEST.value(),
                e), HttpStatus.BAD_REQUEST);
    }

    public static <T> ApiResponseDto<T> statusNotFound(T data) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                data,
                HttpStatus.NOT_FOUND.value()
        ), HttpStatus.NOT_FOUND);
    }

    public static <T> ApiResponseDto<T> statusBadRequest(T data) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                data,
                HttpStatus.BAD_REQUEST.value()
        ),HttpStatus.BAD_REQUEST);
    }
}
