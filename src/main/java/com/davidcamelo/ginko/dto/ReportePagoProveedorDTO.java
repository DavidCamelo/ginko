package com.davidcamelo.ginko.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ReportePagoProveedorDTO(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaInicial,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime fechaFinal,

        BigDecimal totalPagos,
        ProveedorDTO proveedorDTO,

        List<OrdenPagoDTO> ordenesPago
) { }
