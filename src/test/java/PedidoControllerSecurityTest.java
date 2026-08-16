package com.agroconecta;

import com.agroconecta.auth.TokenService;
import com.agroconecta.config.JwtAuthenticationFilter;
import com.agroconecta.config.SecurityConfig;
import com.agroconecta.pedido.PedidoController;
import com.agroconecta.pedido.PedidoService;
import com.agroconecta.pedido.dto.PedidoResponse;
import com.agroconecta.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, TokenService.class})
@TestPropertySource(properties = {
        "app.security.jwt-secret=test-secret-key-for-agroconecta-security-tests-2026",
        "app.security.jwt-expiration-minutes=60"
})
class PedidoControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void usaElClienteDelTokenAunqueLaSolicitudEnvieOtroId() throws Exception {
        when(pedidoService.crear(any(), any()))
                .thenReturn(new PedidoResponse(5003L, "Pendiente", BigDecimal.TEN,
                        BigDecimal.ONE, new BigDecimal("11"), "Pedido registrado correctamente"));

        String solicitud = "{\"clienteId\":999,\"nombreDestinatario\":\"Cliente prueba\","
                + "\"telefono\":\"3000000000\",\"direccion\":\"Calle 1\","
                + "\"ciudad\":\"Sesquile\",\"departamento\":\"Cundinamarca\","
                + "\"metodoPago\":\"contraentrega\","
                + "\"items\":[{\"productoNombre\":\"Ajo\",\"cantidad\":1}]}";

        mockMvc.perform(post("/api/pedidos")
                        .header("Authorization", "Bearer " + tokenCliente())
                        .contentType("application/json")
                        .content(solicitud))
                .andExpect(status().isCreated());

        verify(pedidoService).crear(any(), org.mockito.ArgumentMatchers.eq(25L));
    }

    @Test
    void rechazaPedidoSinAutenticacion() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    private String tokenCliente() {
        Usuario usuario = new Usuario();
        usuario.setId(25L);
        usuario.setNombre("Cliente prueba");
        usuario.setEmail("cliente@agroconecta.test");
        usuario.setRol("cliente");
        return tokenService.generar(usuario);
    }
}
