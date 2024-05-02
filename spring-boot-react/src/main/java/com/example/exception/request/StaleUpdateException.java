package com.example.exception.request;

public class StaleUpdateException extends Exception {
    public StaleUpdateException(String message) {
        super(message);
    }
}
