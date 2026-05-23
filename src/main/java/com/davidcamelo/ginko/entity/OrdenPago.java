package com.davidcamelo.ginko.entity;

import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "orden_pago")
public class OrdenPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    private BigDecimal monto;

    @Column(length = 250)
    private String concepto;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoOrdenPago estado;

    @Column(unique = true)
    private String idempotencyKey;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        estado = EstadoOrdenPago.BORRADOR;
    }
}