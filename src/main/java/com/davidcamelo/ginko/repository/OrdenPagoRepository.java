package com.davidcamelo.ginko.repository;

import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdenPagoRepository extends JpaRepository<OrdenPago, Long> {
    Page<OrdenPago> findAllByProveedorAndEstado(Proveedor proveedor, EstadoOrdenPago estado, Pageable pageRequest);
    Page<OrdenPago> findAllByProveedor(Proveedor proveedor, Pageable pageRequest);
    Page<OrdenPago> findAllByEstado(EstadoOrdenPago estado, Pageable pageable);
    List<OrdenPago> findAllByProveedorAndEstadoAndFechaCreacionBetween(Proveedor proveedor, EstadoOrdenPago estado, LocalDateTime fechaCreacionAfter, LocalDateTime fechaCreacionBefore);
    List<OrdenPago> findAllByProveedorAndFechaCreacionAfter(Proveedor proveedor, LocalDateTime fechaCreacionAfter);
    List<OrdenPago> findAllByProveedorAndFechaCreacionBefore(Proveedor proveedor, LocalDateTime fechaCreacionBefore);
    List<OrdenPago> findAllByProveedor(Proveedor proveedor);
    List<OrdenPago> findAllByEstadoAndFechaCreacionBefore(EstadoOrdenPago estado, LocalDateTime fechaCreacion);
}