package com.davidcamelo.ginko.repository;

import com.davidcamelo.ginko.entity.OrdenPago;
import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenPagoRepository extends JpaRepository<OrdenPago, Long> {
    Page<OrdenPago> findAllByProveedorAndEstado(Proveedor proveedor, EstadoOrdenPago estado, Pageable pageRequest);
    Page<OrdenPago> findAllByProveedor(Proveedor proveedor, Pageable pageRequest);
    Page<OrdenPago> findAllByEstado(EstadoOrdenPago estado, Pageable pageable);
}
