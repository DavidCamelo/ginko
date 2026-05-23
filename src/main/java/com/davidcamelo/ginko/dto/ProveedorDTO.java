package com.davidcamelo.ginko.dto;

import com.davidcamelo.ginko.enums.EstadoProveedor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProveedorDTO(
        Long id,

        @NotBlank(message = "El nombre o razon social es requerido")
        String nombreRazonSocial,

        @NotNull(message = "La identificacion tributaria es requerida")
        Long identificacionTributaria,

        @Email(message = "Correo electronico invalido")
        String correoElectronico,

        EstadoProveedor estado
) { }
