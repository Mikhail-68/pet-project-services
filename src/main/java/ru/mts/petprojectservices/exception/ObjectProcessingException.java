package ru.mts.petprojectservices.exception;

public class ObjectProcessingException extends RuntimeException {
    public ObjectProcessingException() {
        super();
    }

    public ObjectProcessingException(String s) {
        super(s);
    }

    public ObjectProcessingException(Throwable t) {
        super(t);
    }
}
