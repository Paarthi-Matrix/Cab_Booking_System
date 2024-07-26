package com.i2i.zapcab.exception;

public class UnexpectedException extends RuntimeException {
    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnexpectedException(String message) {
        super(message);
    }
}
