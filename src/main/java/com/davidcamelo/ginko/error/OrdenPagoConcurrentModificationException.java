package com.davidcamelo.ginko.error;

public class OrdenPagoConcurrentModificationException extends RuntimeException {

    public OrdenPagoConcurrentModificationException(String message) {
        super(message);
    }
}