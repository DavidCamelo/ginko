package com.davidcamelo.ginko.api;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.davidcamelo.ginko.service.OrdenPagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Orden de Pago API")
@RequiredArgsConstructor
@RestController
@RequestMapping("orden-pago")
public class OrdenPagoController {
    private final OrdenPagoService ordenPagoService;

    @Operation(summary = "Crear orden de pago", description = "Crea una nueva orden de pago")
    @PostMapping
    public ResponseEntity<OrdenPagoDTO> crearOrdenPago(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody OrdenPagoDTO ordenPagoDTO) {
        return ResponseEntity.ok(ordenPagoService.crearOrdenPago(idempotencyKey, ordenPagoDTO));
    }

    @Operation(summary = "Obtener orden de pago", description = "Obtiene una orden de pago por su ID")
    @GetMapping("{id}")
    public ResponseEntity<OrdenPagoDTO> obtenerOrdenPago(@PathVariable Long id) {
        return ResponseEntity.ok(ordenPagoService.obtenerOrdenPago(id));
    }

    @Operation(summary = "Obtener ordenes de pago", description = "Obtiene las ordenes de pago por orden de estado, proveedor o TODOS")
    @GetMapping
    public ResponseEntity<Page<OrdenPagoDTO>> obtenerOrdenesPago(
            @RequestParam(required = false) EstadoOrdenPago estado,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(ordenPagoService.obtenerOrdenesPago(estado, proveedorId, pageNumber, pageSize));
    }

    @Operation(summary = "Obtener ordenes de pago por fechas", description = "Obtiene las ordenes de pago con fecha de vencimiento entre la fecha inicial y la fecha final")
    @GetMapping("obtener-por-fechas")
    public ResponseEntity<List<OrdenPagoDTO>> obtenerOrdenesPorFechas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {
        return ResponseEntity.ok(ordenPagoService.obtenerOrdenesPorFechas(fechaInicio, fechaFin));
    }

    @Operation(summary = "Transicionar Estado orden de pago", description = "Trancisiona el estado de una orden de pago")
    @PutMapping("{id}/estado/{estado}")
    public ResponseEntity<OrdenPagoDTO> transicionarEstadoOrdenPago(@PathVariable Long id, @PathVariable EstadoOrdenPago estado) {
        return ResponseEntity.ok(ordenPagoService.transicionarEstadoOrdenPago(id, estado));
    }
}
