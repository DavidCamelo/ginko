package com.davidcamelo.ginko.dto;

import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record OrdenPagoDTO(
        Long id,

        @NotNull(message = "El proveedor es requerido")
        ProveedorDTO proveedor,

        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal monto,

        @NotBlank(message = "Username is required")
        @Size(max = 250, message = "El concepto no puede tener mas de 250 caracteres")
        String concepto,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaCreacion,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate fechaVencimiento,

        EstadoOrdenPago estado
) { }
