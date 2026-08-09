import com.agroconecta.auth.AuthService;
import com.agroconecta.auth.InvalidCredentialsException;
import com.agroconecta.auth.PasswordHashService;
import com.agroconecta.auth.dto.AuthResponse;
import com.agroconecta.auth.dto.LoginRequest;
import com.agroconecta.usuario.Usuario;
import com.agroconecta.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private UsuarioRepository usuarioRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        authService = new AuthService(usuarioRepository, new PasswordHashService());
    }

    @Test
    void iniciaSesionConCredencialesValidas() {
        Usuario admin = usuarioAdminActivo();
        when(usuarioRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));

        AuthResponse response = authService.iniciarSesion(loginRequest(admin.getEmail(), "AgroAdmin2026"));

        assertEquals("Autenticacion satisfactoria", response.getMensaje());
        assertEquals("admin", response.getRol());
        assertEquals(admin.getEmail(), response.getEmail());
    }

    @Test
    void rechazaUnaContrasenaIncorrecta() {
        Usuario admin = usuarioAdminActivo();
        when(usuarioRepository.findByEmail(admin.getEmail())).thenReturn(Optional.of(admin));

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.iniciarSesion(loginRequest(admin.getEmail(), "incorrecta"))
        );
    }

    private Usuario usuarioAdminActivo() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Administradora Agroconecta");
        usuario.setEmail("admin@agroconecta.com");
        usuario.setRol("admin");
        usuario.setEstado("activo");
        usuario.setPasswordHash(new PasswordHashService().hash("AgroAdmin2026"));
        return usuario;
    }

    private LoginRequest loginRequest(String email, String password) {
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        return request;
    }
}
