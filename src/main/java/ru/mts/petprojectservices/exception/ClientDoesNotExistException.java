package ru.mts.petprojectservices.exception;

public class ClientDoesNotExistException extends RuntimeException {
    public ClientDoesNotExistException() {
        super();
    }

    public ClientDoesNotExistException(String s) {
        super(s);
    }
}
