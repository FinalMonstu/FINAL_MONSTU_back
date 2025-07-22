package com.icetea.MonStu.exception;

public class GoogleResourceExhaustedException extends RuntimeException{
    public GoogleResourceExhaustedException(String message) {
        super(message);
    }

    public GoogleResourceExhaustedException(String message, Throwable cause) {
        super(message, cause);
    }
}
