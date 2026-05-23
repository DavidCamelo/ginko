package com.davidcamelo.ginko.api;

import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.enums.EstadoProveedor;
import com.davidcamelo.ginko.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Proveedor API")
@RequiredArgsConstructor
@RestController
@RequestMapping("proveedor")
public class ProveedorController {
    private final ProveedorService proveedorService;

    @Operation(summary = "Crear proveedor", description = "Crea un nuevo proveedor")
    @PostMapping
    public ResponseEntity<ProveedorDTO> crearProveedor(@Valid @RequestBody ProveedorDTO proveedorDTO) {
        return ResponseEntity.ok(proveedorService.crearProveedor(proveedorDTO));
    }

    @Operation(summary = "Obtener proveedor", description = "Obtiene un proveedor por su ID")
    @GetMapping("{id}")
    public ResponseEntity<ProveedorDTO> obtenerProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.obtenerProveedor(id));
    }

    @Operation(summary = "Obtener proveedores", description = "Obtiene los proveedores por estado del proveedor o TODOS")
    @GetMapping
    public ResponseEntity<Page<ProveedorDTO>> obtenerProveedores(
            @RequestParam(required = false) EstadoProveedor estado,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(proveedorService.obtenerProveedores(estado, pageNumber, pageSize));
    }

    @Operation(summary = "Actualizar proveedor", description = "Actualiza un proveedor existente")
    @PutMapping("{id}")
    public ResponseEntity<ProveedorDTO> actualizarProveedor(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO proveedorDTO) {
        return ResponseEntity.ok(proveedorService.actualizarProveedor(id, proveedorDTO));
    }

    @Operation(summary = "Cambiar estado proveedor", description = "Cambia el estado de un proveedor")
    @PutMapping("{id}/estado")
    public ResponseEntity<ProveedorDTO> cambiarEstadoProveedor(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.cambiarEstadoProveedor(id));
    }
}
