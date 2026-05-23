package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import com.davidcamelo.ginko.error.OrdenPagoNoEncontradoException;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.error.TransicionOrdenPagoException;
import com.davidcamelo.ginko.repository.OrdenPagoRepository;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class OrdenPagoServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private OrdenPagoRepository ordenPagoRepository;

    @InjectMocks
    private OrdenPagoService ordenPagoService;

    private Proveedor proveedor;
    private OrdenPago ordenPago;
    private OrdenPagoDTO ordenPagoDTO;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            log.info("Configurando test {}", mocks);
            proveedor = new Proveedor();
            proveedor.setId(1L);
            proveedor.setNombreRazonSocial("Test Razon Social");
            proveedor.setIdentificacionTributaria(123456789L);
            proveedor.setCorreoElectronico("correo@mail.com");
            proveedor.setEstado(EstadoProveedor.ACTIVO);
            var proveedorDTO = ProveedorDTO.builder()
                    .id(1L)
                    .nombreRazonSocial("Test Razon Social")
                    .identificacionTributaria(123456789L)
                    .correoElectronico("correo@mail.com")
                    .estado(EstadoProveedor.ACTIVO)
                    .build();
            var fechaCreacion = LocalDateTime.now();
            ordenPago = new OrdenPago();
            ordenPago.setId(1L);
            ordenPago.setProveedor(proveedor);
            ordenPago.setMonto(BigDecimal.TEN);
            ordenPago.setConcepto("Concepto");
            ordenPago.setFechaCreacion(fechaCreacion);
            ordenPago.setEstado(EstadoOrdenPago.BORRADOR);
            ordenPagoDTO = OrdenPagoDTO.builder()
                    .id(1L)
                    .proveedor(proveedorDTO)
                    .monto(BigDecimal.TEN)
                    .concepto("Concepto")
                    .fechaCreacion(fechaCreacion)
                    .estado(EstadoOrdenPago.BORRADOR)
                    .build();
        } catch (Exception e) {
            log.error("Error configurando test", e);
        }
    }

    @Test
    void crearOrdenPago_Success() {
        when(proveedorRepository.findByIdAndEstado(1L, EstadoProveedor.ACTIVO)).thenReturn(Optional.of(proveedor));
        when(ordenPagoRepository.save(any(OrdenPago.class))).thenReturn(ordenPago);

        OrdenPagoDTO result = ordenPagoService.crearOrdenPago(ordenPagoDTO);

        assertNotNull(result);
        assertEquals(ordenPago.getId(), result.id());
        verify(ordenPagoRepository).save(any(OrdenPago.class));
    }

    @Test
    void crearOrdenPago_ProveedorNoActivo() {
        when(proveedorRepository.findByIdAndEstado(1L, EstadoProveedor.ACTIVO)).thenReturn(Optional.empty());
        assertThrows(ProveedorNoEncontradoException.class, () -> ordenPagoService.crearOrdenPago(ordenPagoDTO));
    }

    @Test
    void obtenerOrdenPago_Success() {
        when(ordenPagoRepository.findById(1L)).thenReturn(Optional.of(ordenPago));
        var result = ordenPagoService.obtenerOrdenPago(1L);
        assertNotNull(result);
        assertEquals(ordenPago.getId(), result.id());
    }

    @Test
    void obtenerOrdenPago_NoEncontrado() {
        when(ordenPagoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrdenPagoNoEncontradoException.class, () -> ordenPagoService.obtenerOrdenPago(1L));
    }

    @Test
    void obtenerOrdenesPago_SinFiltros() {
        var page = new PageImpl<>(Collections.singletonList(ordenPago));
        when(ordenPagoRepository.findAll(any(PageRequest.class))).thenReturn(page);
        var result = ordenPagoService.obtenerOrdenesPago(null, null, 0, 10);
        assertEquals(1, result.getTotalElements());
    }
    
    @Test
    void obtenerOrdenesPago_FiltroEstado() {
        var page = new PageImpl<>(Collections.singletonList(ordenPago));
        when(ordenPagoRepository.findAllByEstado(any(EstadoOrdenPago.class), any(PageRequest.class))).thenReturn(page);
        var result = ordenPagoService.obtenerOrdenesPago(EstadoOrdenPago.BORRADOR, null, 0, 10);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void transicionarEstadoOrdenPago_BorradorAAprobada() {
        when(ordenPagoRepository.findById(1L)).thenReturn(Optional.of(ordenPago));
        when(ordenPagoRepository.save(any(OrdenPago.class))).thenAnswer(i -> i.getArguments()[0]);
        var result = ordenPagoService.transicionarEstadoOrdenPago(1L, EstadoOrdenPago.APROBADA);
        assertEquals(EstadoOrdenPago.APROBADA, result.estado());
    }

    @Test
    void transicionarEstadoOrdenPago_AprobadaAPagada() {
        ordenPago.setEstado(EstadoOrdenPago.APROBADA);
        when(ordenPagoRepository.findById(1L)).thenReturn(Optional.of(ordenPago));
        when(ordenPagoRepository.save(any(OrdenPago.class))).thenAnswer(i -> i.getArguments()[0]);
        var result = ordenPagoService.transicionarEstadoOrdenPago(1L, EstadoOrdenPago.PAGADA);
        assertEquals(EstadoOrdenPago.PAGADA, result.estado());
    }

    @Test
    void transicionarEstadoOrdenPago_TransicionInvalida() {
        ordenPago.setEstado(EstadoOrdenPago.APROBADA);
        when(ordenPagoRepository.findById(1L)).thenReturn(Optional.of(ordenPago));
        assertThrows(TransicionOrdenPagoException.class, () -> ordenPagoService.transicionarEstadoOrdenPago(1L, EstadoOrdenPago.BORRADOR));
    }
}
