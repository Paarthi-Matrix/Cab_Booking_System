package com.i2i.zapcab.exception;

public class ZapCabException extends RuntimeException{
    public ZapCabException(String message, Throwable throwable){
        super(message, throwable);
    }
}
