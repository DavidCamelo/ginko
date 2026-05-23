package com.davidcamelo.ginko.mapper;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.entity.OrdenPago;
import org.springframework.data.domain.Page;

public class OrdenPagoMapper {

    public static void mapToOrdenPago(OrdenPagoDTO ordenPagoDTO, OrdenPago ordenPago) {
        ordenPago.setMonto(ordenPagoDTO.monto());
        ordenPago.setConcepto(ordenPagoDTO.concepto());
    }

    public static OrdenPagoDTO mapToOrdenPagoDTO(OrdenPago ordenPago) {
        return OrdenPagoDTO.builder()
                .id(ordenPago.getId())
                .proveedor(ProveedorMapper.mapToProveedorDTO(ordenPago.getProveedor()))
                .monto(ordenPago.getMonto())
                .concepto(ordenPago.getConcepto())
                .fechaCreacion(ordenPago.getFechaCreacion())
                .estado(ordenPago.getEstado())
                .build();
    }

    public static Page<OrdenPagoDTO> mapToOrdenPagoDTOPage(Page<OrdenPago> ordenPagoPage) {
        return ordenPagoPage.map(OrdenPagoMapper::mapToOrdenPagoDTO);
    }
}
