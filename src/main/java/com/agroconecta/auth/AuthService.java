package com.agroconecta.auth;

import com.agroconecta.auth.dto.AuthResponse;
import com.agroconecta.auth.dto.LoginRequest;
import com.agroconecta.auth.dto.RegisterRequest;
import com.agroconecta.usuario.Usuario;
import com.agroconecta.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordHashService passwordHashService;
    private final TokenService tokenService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordHashService passwordHashService, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHashService = passwordHashService;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        usuarioRepository.findByEmail(request.getEmail())
                .ifPresent(usuario -> {
                    throw new IllegalArgumentException("El correo ya se encuentra registrado");
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol("cliente");
        usuario.setEstado("activo");
        // La contrasena no se guarda en texto plano; se almacena su hash para proteger el dato sensible.
        usuario.setPasswordHash(passwordHashService.hash(request.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return new AuthResponse(
                "Usuario registrado correctamente",
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol(),
                tokenService.generar(usuarioGuardado)
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse iniciarSesion(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Error en la autenticacion"));

        if (!"activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new InvalidCredentialsException("Error en la autenticacion");
        }

        boolean passwordCorrecta = passwordHashService.matches(request.getPassword(), usuario.getPasswordHash());
        if (!passwordCorrecta) {
            throw new InvalidCredentialsException("Error en la autenticacion");
        }

        return new AuthResponse(
                "Autenticacion satisfactoria",
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                tokenService.generar(usuario)
        );
    }
}
