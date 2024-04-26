package ru.mts.petprojectservices.exception;

public class RequestDoesNotExistException extends RuntimeException {
    public RequestDoesNotExistException() {
    }

    public RequestDoesNotExistException(String message) {
        super(message);
    }
}
