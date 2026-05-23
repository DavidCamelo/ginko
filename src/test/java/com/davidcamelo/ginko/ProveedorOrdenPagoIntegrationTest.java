package com.davidcamelo.ginko;

import com.davidcamelo.ginko.dto.OrdenPagoDTO;
import com.davidcamelo.ginko.dto.ProveedorDTO;
import com.davidcamelo.ginko.enums.EstadoOrdenPago;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProveedorOrdenPagoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crearProveedorYOrdenDePagoYTransicionar() throws Exception {
        var jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());

        var proveedorDTO = ProveedorDTO.builder()
                .nombreRazonSocial("Test Razon Social")
                .identificacionTributaria(123456789L)
                .correoElectronico("correo@mail.com")
                .build();
        var proveedorResult = mockMvc.perform(post("/proveedor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(proveedorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        var proveedorResponse = proveedorResult.getResponse().getContentAsString();
        var proveedorDTOCreado = jsonMapper.readValue(proveedorResponse, ProveedorDTO.class);

        var ordenPagoDTO = OrdenPagoDTO.builder()
                .proveedor(proveedorDTOCreado)
                .monto(BigDecimal.TEN)
                .concepto("Concepto")
                .build();
        var idempotencyKey = UUID.randomUUID().toString();
        var ordenPagoResult = mockMvc.perform(post("/orden-pago")
                        .header("Idempotency-Key", idempotencyKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(ordenPagoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        var ordenPagoResponse = ordenPagoResult.getResponse().getContentAsString();
        var ordenPagoDTOCreada = jsonMapper.readValue(ordenPagoResponse, OrdenPagoDTO.class);

        mockMvc.perform(put("/orden-pago/{id}/estado/{estado}", ordenPagoDTOCreada.id(), EstadoOrdenPago.APROBADA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));
    }
}