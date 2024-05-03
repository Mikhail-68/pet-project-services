package ru.mts.petprojectservices.exception;

public class IncorrectStatusNameException extends IllegalArgumentException {
    public IncorrectStatusNameException() {
        super();
    }

    public IncorrectStatusNameException(String s) {
        super(s);
    }
}
