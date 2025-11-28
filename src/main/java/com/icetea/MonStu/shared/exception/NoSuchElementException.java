package com.icetea.MonStu.shared.exception;

public class NoSuchElementException extends RuntimeException {
    public NoSuchElementException(String message) { super(message); }

    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }
}


