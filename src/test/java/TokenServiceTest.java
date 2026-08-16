import com.agroconecta.auth.TokenService;
import com.agroconecta.usuario.Usuario;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenServiceTest {

    @Test
    void generaYValidaTokenConRol() {
        Usuario usuario = new Usuario();
        usuario.setId(4L);
        usuario.setNombre("Administradora Agroconecta");
        usuario.setEmail("admin@agroconecta.com");
        usuario.setRol("admin");

        TokenService service = new TokenService(
                "test-secret-key-for-agroconecta-security-tests-2026",
                60
        );

        Claims claims = service.validar(service.generar(usuario));

        assertEquals(usuario.getEmail(), claims.getSubject());
        assertEquals("admin", claims.get("rol"));
        assertEquals(4, claims.get("usuarioId"));
    }
}
