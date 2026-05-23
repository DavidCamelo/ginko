package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import com.davidcamelo.ginko.error.IdentificacionTributariaException;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    private Proveedor proveedor;
    private ProveedorDTO proveedorDTO;

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
            proveedorDTO = ProveedorDTO.builder()
                    .id(1L)
                    .nombreRazonSocial("Test Razon Social")
                    .identificacionTributaria(123456789L)
                    .correoElectronico("correo@mail.com")
                    .estado(EstadoProveedor.ACTIVO)
                    .build();
        } catch (Exception e) {
            log.error("Error configurando test", e);
        }
    }

    @DisplayName("Crear un proveedor")
    @Test
    void crearProveedor() {
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);
        var result = proveedorService.crearProveedor(proveedorDTO);
        assertNotNull(result);
        assertEquals(proveedor.getNombreRazonSocial(), result.nombreRazonSocial());
        assertEquals(proveedor.getIdentificacionTributaria(), result.identificacionTributaria());
        assertEquals(proveedor.getCorreoElectronico(), result.correoElectronico());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @DisplayName("Crear un proveedor con identificacion tributaria duplicada")
    @Test
    void crearProveedor_conIdentificacionTributariaDuplicada() {
        when(proveedorRepository.save(any(Proveedor.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(IdentificacionTributariaException.class, () -> proveedorService.crearProveedor(proveedorDTO));
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @DisplayName("Obtener un proveedor")
    @Test
    void obtenerProveedor() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        var result = proveedorService.obtenerProveedor(1L);
        assertNotNull(result);
        assertEquals(proveedor.getId(), result.id());
        assertEquals(proveedor.getNombreRazonSocial(), result.nombreRazonSocial());
        assertEquals(proveedor.getIdentificacionTributaria(), result.identificacionTributaria());
        assertEquals(proveedor.getCorreoElectronico(), result.correoElectronico());
        assertEquals(proveedor.getEstado(), result.estado());
    }

    @DisplayName("Obtener un proveedor no encontrado")
    @Test
    void obtenerProveedor_noEncontrado() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProveedorNoEncontradoException.class, () -> proveedorService.obtenerProveedor(1L));
    }

    @DisplayName("Obtener proveedores")
    @Test
    void obtenerProveedores() {
        var page = new PageImpl<>(Collections.singletonList(proveedor));
        when(proveedorRepository.findAll(any(PageRequest.class))).thenReturn(page);
        var result = proveedorService.obtenerProveedores(null, 0, 10);
        assertEquals(1, result.getTotalElements());
        verify(proveedorRepository).findAll(any(PageRequest.class));
    }

    @DisplayName("Obtener proveedores con estado")
    @Test
    void obtenerProveedores_conEstado() {
        var page = new PageImpl<>(Collections.singletonList(proveedor));
        when(proveedorRepository.findAllByEstado(any(EstadoProveedor.class), any(PageRequest.class))).thenReturn(page);
        var result = proveedorService.obtenerProveedores(EstadoProveedor.ACTIVO, 0, 10);
        assertEquals(1, result.getTotalElements());
        verify(proveedorRepository).findAllByEstado(any(EstadoProveedor.class), any(PageRequest.class));
    }

    @DisplayName("Actualizar un proveedor")
    @Test
    void actualizarProveedor() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);
        var result = proveedorService.actualizarProveedor(1L, proveedorDTO);
        assertNotNull(result);
        assertEquals(proveedor.getId(), result.id());
        assertEquals(proveedor.getNombreRazonSocial(), result.nombreRazonSocial());
        assertEquals(proveedor.getIdentificacionTributaria(), result.identificacionTributaria());
        assertEquals(proveedor.getCorreoElectronico(), result.correoElectronico());
        assertEquals(proveedor.getEstado(), result.estado());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @DisplayName("Cambias estado a un proveedor")
    @Test
    void cambiarEstadoProveedor() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(proveedorRepository.save(any(Proveedor.class))).thenReturn(proveedor);
        var result = proveedorService.cambiarEstadoProveedor(1L);
        assertEquals(EstadoProveedor.INACTIVO, result.estado());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @DisplayName("Eliminar un proveedor")
    @Test
    void eliminarProveedor() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        doNothing().when(proveedorRepository).delete(proveedor);
        proveedorService.eliminarProveedor(1L);
        verify(proveedorRepository).delete(proveedor);
    }
}
