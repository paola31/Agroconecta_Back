import com.agroconecta.auth.AuthService;
import com.agroconecta.auth.PasswordHashService;
import com.agroconecta.auth.TokenService;
import com.agroconecta.auth.dto.AuthResponse;
import com.agroconecta.auth.dto.RegisterRequest;
import com.agroconecta.usuario.Usuario;
import com.agroconecta.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthRegistrationRoleTest {

    @Test
    void elRegistroPublicoSiempreCreaUnCliente() {
        UsuarioRepository repository = mock(UsuarioRepository.class);
        when(repository.findByEmail("nuevo@agroconecta.test")).thenReturn(Optional.empty());
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(20L);
            return usuario;
        });

        AuthService service = new AuthService(
                repository,
                new PasswordHashService(),
                new TokenService("test-secret-key-for-agroconecta-security-tests-2026", 60)
        );
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Nuevo usuario");
        request.setEmail("nuevo@agroconecta.test");
        request.setTelefono("3000000000");
        request.setPassword("ClaveSegura2026");
        request.setRol("admin");

        AuthResponse response = service.registrar(request);

        assertEquals("cliente", response.getRol());
    }
}
