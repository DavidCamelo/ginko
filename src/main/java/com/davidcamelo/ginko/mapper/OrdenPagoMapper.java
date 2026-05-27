package com.davidcamelo.ginko.mapper;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.entity.OrdenPago;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class OrdenPagoMapper {

    public static void mapToOrdenPago(OrdenPagoDTO ordenPagoDTO, OrdenPago ordenPago) {
        ordenPago.setMonto(ordenPagoDTO.monto());
        ordenPago.setConcepto(ordenPagoDTO.concepto());
        ordenPago.setFechaVencimiento(ordenPagoDTO.fechaVencimiento());
    }

    public static OrdenPagoDTO mapToOrdenPagoDTO(OrdenPago ordenPago) {
        return OrdenPagoDTO.builder()
                .id(ordenPago.getId())
                .proveedor(ProveedorMapper.mapToProveedorDTO(ordenPago.getProveedor()))
                .monto(ordenPago.getMonto())
                .concepto(ordenPago.getConcepto())
                .fechaCreacion(ordenPago.getFechaCreacion())
                .fechaVencimiento(ordenPago.getFechaVencimiento())
                .estado(ordenPago.getEstado())
                .build();
    }

    public static List<OrdenPagoDTO> mapToOrdenPagoDTOList(List<OrdenPago> ordenesPago) {
        return Optional.ofNullable(ordenesPago)
                .stream()
                .flatMap(Collection::stream)
                .map(OrdenPagoMapper::mapToOrdenPagoDTO)
                .toList();
    }

    public static Page<OrdenPagoDTO> mapToOrdenPagoDTOPage(Page<OrdenPago> ordenPagoPage) {
        return ordenPagoPage.map(OrdenPagoMapper::mapToOrdenPagoDTO);
    }
}
