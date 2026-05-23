package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.repository.OrdenPagoRepository;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
class ReporteServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private OrdenPagoRepository ordenPagoRepository;

    @InjectMocks
    private ReporteService reporteService;

    private Proveedor proveedor;
    private OrdenPago ordenPago;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            log.info("Configurando test {}", mocks);
            proveedor = new Proveedor();
            proveedor.setId(1L);

            ordenPago = new OrdenPago();
            ordenPago.setId(1L);
            ordenPago.setProveedor(proveedor);
            ordenPago.setMonto(BigDecimal.TEN);
            ordenPago.setEstado(EstadoOrdenPago.PAGADA);
            ordenPago.setFechaCreacion(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error configurando test", e);
        }
    }

    @Test
    void obtenerReportePagosPorProveedor_Success() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(ordenPagoRepository.findAllByProveedor(proveedor)).thenReturn(Collections.singletonList(ordenPago));
        var result = reporteService.obtenerReportePagosPorProveedor(1L, null, null);
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.totalPagos());
        assertEquals(1, result.ordenesPago().size());
    }

    @Test
    void obtenerReportePagosPorProveedor_ProveedorNoEncontrado() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProveedorNoEncontradoException.class, () -> reporteService.obtenerReportePagosPorProveedor(1L, null, null));
    }

    @Test
    void obtenerReportePagosPorProveedor_ConFechas() {
        var fechaInicial = LocalDateTime.now().minusDays(1);
        var fechaFinal = LocalDateTime.now().plusDays(1);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(ordenPagoRepository.findAllByProveedorAndEstadoAndFechaCreacionBetween(proveedor, EstadoOrdenPago.PAGADA, fechaInicial, fechaFinal)).thenReturn(Collections.singletonList(ordenPago));
        var result = reporteService.obtenerReportePagosPorProveedor(1L, fechaInicial, fechaFinal);
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.totalPagos());
    }
    
    @Test
    void obtenerReportePagosPorProveedor_ConFechaInicial() {
        var fechaInicial = LocalDateTime.now().minusDays(1);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(ordenPagoRepository.findAllByProveedorAndFechaCreacionAfter(proveedor, fechaInicial)) .thenReturn(Collections.singletonList(ordenPago));
        var result = reporteService.obtenerReportePagosPorProveedor(1L, fechaInicial, null);
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.totalPagos());
    }

    @Test
    void obtenerReportePagosPorProveedor_ConFechaFinal() {
        var fechaFinal = LocalDateTime.now().plusDays(1);
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(ordenPagoRepository.findAllByProveedorAndFechaCreacionBefore(proveedor, fechaFinal)).thenReturn(Collections.singletonList(ordenPago));
        var result = reporteService.obtenerReportePagosPorProveedor(1L, null, fechaFinal);
        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.totalPagos());
    }

    @Test
    void obtenerOrdenesProximasAVencer() {
        ordenPago.setEstado(EstadoOrdenPago.APROBADA);
        ordenPago.setFechaCreacion(LocalDateTime.now().minusDays(91));
        when(ordenPagoRepository.findAllByEstadoAndFechaCreacionBefore(eq(EstadoOrdenPago.APROBADA), any(LocalDateTime.class))).thenReturn(Collections.singletonList(ordenPago));
        var result = reporteService.obtenerOrdenesProximasAVencer();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ordenPago.getId(), result.getFirst().id());
    }
}