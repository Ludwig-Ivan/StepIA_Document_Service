package com.stepia.document_service.exception;

public class ObjectNotFoundException
        extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }
}