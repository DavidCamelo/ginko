package com.davidcamelo.ginko.entity;

import com.davidcamelo.ginko.enums.EstadoProveedor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nombreRazonSocial;

    @Column(unique = true)
    private Long identificacionTributaria;

    @Column(length = 40)
    private String correoElectronico;

    @Column(length = 15)
    @Enumerated(EnumType.STRING)
    private EstadoProveedor estado;

    @PrePersist
    public void prePersist() {
        estado = EstadoProveedor.ACTIVO;
    }
}
