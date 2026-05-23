package com.davidcamelo.ginko.api;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.dto.ReportePagoProveedorDTO;
import com.davidcamelo.ginko.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Reporte API")
@RequiredArgsConstructor
@RestController
@RequestMapping("reporte")
public class ReporteController {
    private final ReporteService reporteService;

    @Operation(summary = "Obtener reporte de pagos por proveedor", description = "Obtiene el monto total pagado a cada proveedor")
    @GetMapping("pagos-proveedor/{proveedorId}")
    public ResponseEntity<ReportePagoProveedorDTO> obtenerReportePagosPorProveedor(
            @PathVariable Long proveedorId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaInicial,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fechaFinal) {
        return ResponseEntity.ok(reporteService.obtenerReportePagosPorProveedor(proveedorId, fechaInicial, fechaFinal));
    }

    @Operation(summary = "Obtener ordenes de pago proximas a vencer", description = "Obtiene las ordenes de pago con mas de 90 dias en estado APROBADA")
    @GetMapping("ordenes-pago/proximas-a-vencer")
    public ResponseEntity<List<OrdenPagoDTO>> obtenerOrdenesProximasAVencer() {
        return ResponseEntity.ok(reporteService.obtenerOrdenesProximasAVencer());
    }
}