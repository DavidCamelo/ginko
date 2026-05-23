package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.dto.ReportePagoProveedorDTO;
import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.mapper.OrdenPagoMapper;
import com.davidcamelo.ginko.mapper.ProveedorMapper;
import com.davidcamelo.ginko.repository.OrdenPagoRepository;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReporteService {
    private final ProveedorRepository proveedorRepository;
    private final OrdenPagoRepository ordenPagoRepository;

    public ReportePagoProveedorDTO obtenerReportePagosPorProveedor(Long proveedorId, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        var proveedor = proveedorRepository.findById(proveedorId)
                .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor no encontrado"));
        List<OrdenPago> ordenesPago;
        if (fechaInicial != null && fechaFinal != null) {
            ordenesPago = ordenPagoRepository.findAllByProveedorAndEstadoAndFechaCreacionBetween(proveedor, EstadoOrdenPago.PAGADA, fechaInicial, fechaFinal);
        } else if (fechaInicial != null) {
            ordenesPago = ordenPagoRepository.findAllByProveedorAndFechaCreacionAfter(proveedor, fechaInicial);
        } else if (fechaFinal != null) {
            ordenesPago = ordenPagoRepository.findAllByProveedorAndFechaCreacionBefore(proveedor, fechaFinal);
        } else {
            ordenesPago = ordenPagoRepository.findAllByProveedor(proveedor);
        }
        var totalPagos = ordenesPago.stream()
                .map(OrdenPago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ReportePagoProveedorDTO.builder()
                .fechaInicial(fechaInicial)
                .fechaFinal(fechaFinal)
                .totalPagos(totalPagos)
                .proveedorDTO(ProveedorMapper.mapToProveedorDTO(proveedor))
                .ordenesPago(OrdenPagoMapper.mapToOrdenPagoDTOList(ordenesPago))
                .build();
    }

    public List<OrdenPagoDTO> obtenerOrdenesProximasAVencer() {
        var fechaLimite = LocalDateTime.now().minusDays(90);
        var ordenes = ordenPagoRepository.findAllByEstadoAndFechaCreacionBefore(EstadoOrdenPago.APROBADA, fechaLimite);
        return OrdenPagoMapper.mapToOrdenPagoDTOList(ordenes);
    }
}