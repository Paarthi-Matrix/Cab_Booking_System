package com.i2i.zapcab.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
