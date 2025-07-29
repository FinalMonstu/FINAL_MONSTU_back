package com.icetea.MonStu.exception;

public class EmptyParameterException extends RuntimeException{
    public EmptyParameterException(String message) {
        super(message);
    }

    public EmptyParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
