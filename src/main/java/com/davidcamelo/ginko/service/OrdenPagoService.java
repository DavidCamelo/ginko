package com.davidcamelo.ginko.service;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import com.davidcamelo.ginko.error.OrdenPagoConcurrentModificationException;
import com.davidcamelo.ginko.error.OrdenPagoNoEncontradoException;
import com.davidcamelo.ginko.error.ProveedorNoEncontradoException;
import com.davidcamelo.ginko.error.TransicionOrdenPagoException;
import com.davidcamelo.ginko.mapper.OrdenPagoMapper;
import com.davidcamelo.ginko.repository.OrdenPagoRepository;
import com.davidcamelo.ginko.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrdenPagoService {
    private final ProveedorRepository proveedorRepository;
    private final OrdenPagoRepository ordenPagoRepository;

    @Transactional
    public OrdenPagoDTO crearOrdenPago(String idempotencyKey, OrdenPagoDTO ordenPagoDTO) {
        var ordenExistente = ordenPagoRepository.findByIdempotencyKey(idempotencyKey);
        if (ordenExistente.isPresent()) {
            return OrdenPagoMapper.mapToOrdenPagoDTO(ordenExistente.get());
        }
        var proveedor = proveedorRepository.findByIdAndEstado(ordenPagoDTO.proveedor().id(), EstadoProveedor.ACTIVO)
                .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor activo no encontrado"));
        var ordenPago = new OrdenPago();
        ordenPago.setProveedor(proveedor);
        ordenPago.setIdempotencyKey(idempotencyKey);
        OrdenPagoMapper.mapToOrdenPago(ordenPagoDTO, ordenPago);
        return OrdenPagoMapper.mapToOrdenPagoDTO(ordenPagoRepository.save(ordenPago));
    }

    public OrdenPagoDTO obtenerOrdenPago(Long id) {
        return OrdenPagoMapper.mapToOrdenPagoDTO(obtenerOrdenPagoPorId(id));
    }

    public Page<OrdenPagoDTO> obtenerOrdenesPago(EstadoOrdenPago estado, Long proveedorId, Integer pageNumber, Integer pageSize) {
        var pageRequest = PageRequest.of(pageNumber, pageSize);
        Proveedor proveedor = null;
        if (proveedorId != null) {
            proveedor = proveedorRepository.findById(proveedorId)
                    .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor no encontrado"));
        }
        if (proveedor != null && estado != null) {
            return OrdenPagoMapper.mapToOrdenPagoDTOPage(ordenPagoRepository.findAllByProveedorAndEstado(proveedor, estado, pageRequest));
        }
        if (proveedor != null) {
            return OrdenPagoMapper.mapToOrdenPagoDTOPage(ordenPagoRepository.findAllByProveedor(proveedor, pageRequest));
        }
        if (estado != null) {
            return OrdenPagoMapper.mapToOrdenPagoDTOPage(ordenPagoRepository.findAllByEstado(estado, pageRequest));
        }
        return OrdenPagoMapper.mapToOrdenPagoDTOPage(ordenPagoRepository.findAll(pageRequest));
    }

    @Transactional
    public OrdenPagoDTO transicionarEstadoOrdenPago(Long id, EstadoOrdenPago estado) {
        try {
            var ordenPago = obtenerOrdenPagoPorId(id);
            var estadoActual = ordenPago.getEstado();
            if (estadoActual.equals(EstadoOrdenPago.BORRADOR)) {
                if (estado.equals(EstadoOrdenPago.APROBADA) || estado.equals(EstadoOrdenPago.RECHAZADA)) {
                    ordenPago.setEstado(estado);
                    return OrdenPagoMapper.mapToOrdenPagoDTO(ordenPagoRepository.save(ordenPago));
                }
            } else if (estadoActual.equals(EstadoOrdenPago.APROBADA) && estado.equals(EstadoOrdenPago.PAGADA)) {
                ordenPago.setEstado(estado);
                return OrdenPagoMapper.mapToOrdenPagoDTO(ordenPagoRepository.save(ordenPago));
            }
            throw new TransicionOrdenPagoException("No se puede transicionar el estado de la orden de pago de " + estadoActual + " a " + estado);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new OrdenPagoConcurrentModificationException("La orden de pago ha sido modificada por otro usuario. Por favor, intente de nuevo.");
        }
    }

    private OrdenPago obtenerOrdenPagoPorId(Long id) {
        return ordenPagoRepository.findById(id)
                .orElseThrow(() -> new OrdenPagoNoEncontradoException("Orden de pago no encontrada"));
    }
}