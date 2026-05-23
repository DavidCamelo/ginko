package com.davidcamelo.ginko.mapper;

import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.entity.Proveedor;
import org.springframework.data.domain.Page;

public class ProveedorMapper {

    public static void mapToProveedor(ProveedorDTO proveedorDTO, Proveedor proveedor) {
        proveedor.setNombreRazonSocial(proveedorDTO.nombreRazonSocial());
        proveedor.setIdentificacionTributaria(proveedorDTO.identificacionTributaria());
        proveedor.setCorreoElectronico(proveedorDTO.correoElectronico());
    }

    public static ProveedorDTO mapToProveedorDTO(Proveedor proveedor) {
        return ProveedorDTO.builder()
                .id(proveedor.getId())
                .nombreRazonSocial(proveedor.getNombreRazonSocial())
                .identificacionTributaria(proveedor.getIdentificacionTributaria())
                .correoElectronico(proveedor.getCorreoElectronico())
                .estado(proveedor.getEstado())
                .build();
    }

    public static Page<ProveedorDTO> mapToProveedorDTOPage(Page<Proveedor> proveedorPage) {
        return proveedorPage.map(ProveedorMapper::mapToProveedorDTO);
    }
}
