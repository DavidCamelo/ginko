package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import com.davidcamelo.ginko.error.IdentificacionTributariaException;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.mapper.ProveedorMapper;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public ProveedorDTO crearProveedor(ProveedorDTO proveedorDTO) {
        return crearActualizarProveedor(proveedorDTO, new Proveedor());
    }

    public ProveedorDTO obtenerProveedor(Long id) {
        return ProveedorMapper.mapToProveedorDTO(obtenerProveedorPorId(id));
    }

    public Page<ProveedorDTO> obtenerProveedores(EstadoProveedor estado, Integer pageNumber, Integer pageSize) {
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        if (estado != null) {
            return ProveedorMapper.mapToProveedorDTOPage(proveedorRepository.findAllByEstado(estado, pageRequest));
        }
        return ProveedorMapper.mapToProveedorDTOPage(proveedorRepository.findAll(pageRequest));
    }

    public ProveedorDTO actualizarProveedor(Long id, ProveedorDTO proveedorDTO) {
        return crearActualizarProveedor(proveedorDTO, obtenerProveedorPorId(id));
    }

    public ProveedorDTO cambiarEstadoProveedor(Long id) {
        var proveedor = obtenerProveedorPorId(id);
        proveedor.setEstado(proveedor.getEstado() == EstadoProveedor.ACTIVO ? EstadoProveedor.INACTIVO : EstadoProveedor.ACTIVO);
        proveedorRepository.save(proveedor);
        return ProveedorMapper.mapToProveedorDTO(proveedor);
    }

    public void eliminarProveedor(Long id) {
        proveedorRepository.delete(obtenerProveedorPorId(id));
    }

    private ProveedorDTO crearActualizarProveedor(ProveedorDTO proveedorDTO, Proveedor proveedor) {
        try {
            ProveedorMapper.mapToProveedor(proveedorDTO, proveedor);
            proveedorRepository.save(proveedor);
            return ProveedorMapper.mapToProveedorDTO(proveedor);
        } catch (DataIntegrityViolationException ex) {
            throw new IdentificacionTributariaException("La identificacion tributaria ya esta en uso");
        }
    }

    private Proveedor obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor no encontrado"));
    }
}
