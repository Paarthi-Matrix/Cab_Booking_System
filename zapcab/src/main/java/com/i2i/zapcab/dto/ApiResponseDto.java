package com.i2i.zapcab.dto;

import com.i2i.zapcab.common.ErrorMessage;
import com.i2i.zapcab.common.SuccessMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

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

    public static <T> ApiResponseDto<T> statusNoContent(T data, Exception e) {
        return new ApiResponseDto<>(new ErrorMessage<>(
                "No content available",
                data,
                HttpStatus.NO_CONTENT.value(),
                e), HttpStatus.NO_CONTENT);
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
}
