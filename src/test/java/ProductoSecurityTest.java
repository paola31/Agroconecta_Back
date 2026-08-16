package com.agroconecta;

import com.agroconecta.auth.TokenService;
import com.agroconecta.config.JwtAuthenticationFilter;
import com.agroconecta.config.SecurityConfig;
import com.agroconecta.producto.ProductoController;
import com.agroconecta.producto.ProductoService;
import com.agroconecta.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, TokenService.class})
@TestPropertySource(properties = {
        "app.security.jwt-secret=test-secret-key-for-agroconecta-security-tests-2026",
        "app.security.jwt-expiration-minutes=60"
})
class ProductoSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private ProductoService productoService;

    @Test
    void permiteConsultarProductosSinAutenticacion() throws Exception {
        when(productoService.listarActivos()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void rechazaCrearProductoSinAutenticacion() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rechazaCrearProductoConRolCliente() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + tokenPara("cliente"))
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void permiteQueElRolAdministradorLlegueAlControlador() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .header("Authorization", "Bearer " + tokenPara("admin"))
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    private String tokenPara(String rol) {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNombre("Usuario de prueba");
        usuario.setEmail(rol + "@agroconecta.test");
        usuario.setRol(rol);
        return tokenService.generar(usuario);
    }
}
