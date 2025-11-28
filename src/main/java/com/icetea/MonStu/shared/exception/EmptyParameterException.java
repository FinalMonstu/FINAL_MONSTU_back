package com.icetea.MonStu.shared.exception;

public class EmptyParameterException extends RuntimeException{
    public EmptyParameterException(String message) {
        super(message);
    }

    public EmptyParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
