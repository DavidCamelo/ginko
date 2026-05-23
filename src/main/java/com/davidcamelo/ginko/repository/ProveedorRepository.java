package com.davidcamelo.ginko.repository;

import com.davidcamelo.ginko.entity.Proveedor;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Page<Proveedor> findAllByEstado(EstadoProveedor estado, Pageable pageable);

    Optional<Proveedor> findByIdAndEstado(Long id, EstadoProveedor estadoProveedor);
}
