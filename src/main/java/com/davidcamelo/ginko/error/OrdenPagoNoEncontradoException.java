package com.davidcamelo.ginko.error;

public class OrdenPagoNoEncontradoException extends RuntimeException {

    public OrdenPagoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
