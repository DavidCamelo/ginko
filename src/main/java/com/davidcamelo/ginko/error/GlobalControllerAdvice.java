package com.davidcamelo.ginko.error;

import com.davidcamelo.ginko.api.OrdenPagoController;
import com.davidcamelo.ginko.api.ProveedorController;
import com.davidcamelo.ginko.api.ReporteController;
import com.davidcamelo.ginko.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(assignableTypes = { ProveedorController.class, OrdenPagoController.class, ReporteController.class })
public class GlobalControllerAdvice {

    @ExceptionHandler(value = { ProveedorNoEncontradoException.class, OrdenPagoNoEncontradoException.class })
    public ResponseEntity<ErrorDTO> manejarNoEncontradoException(RuntimeException ex) {
        return buildError(ex, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { IdentificacionTributariaException.class })
    public ResponseEntity<ErrorDTO> manejarIdentificacionTributariaException(IdentificacionTributariaException ex) {
        return buildError(ex, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = { TransicionOrdenPagoException.class })
    public ResponseEntity<ErrorDTO> manejarTransicionOrdenPagoException(TransicionOrdenPagoException ex) {
        return buildError(ex, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = { OrdenPagoConcurrentModificationException.class })
    public ResponseEntity<ErrorDTO> manejarOrdenPagoConcurrentModificationException(OrdenPagoConcurrentModificationException ex) {
        return buildError(ex, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorDTO> manejarMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var mensaje = ex.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return buildError(ex, mensaje, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    public ResponseEntity<ErrorDTO> manejarCualquierException(Exception ex) {
        return buildError(ex, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDTO> buildError(Exception ex, String mensaje, HttpStatus status) {
        var errorDTO = ErrorDTO
                .builder()
                .mensaje(mensaje)
                .fecha(LocalDateTime.now())
                .build();
        log.error("Error mensaje: {}, fecha: {}", errorDTO.mensaje(), errorDTO.fecha(), ex);
        return new ResponseEntity<>(errorDTO, status);
    }
}
