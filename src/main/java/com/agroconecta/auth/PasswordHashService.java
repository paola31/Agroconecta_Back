package com.agroconecta.auth;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class PasswordHashService {

    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte item : encodedHash) {
                String hex = Integer.toHexString(0xff & item);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("No fue posible cifrar la contrasena", exception);
        }
    }

    public boolean matches(String password, String passwordHash) {
        // Se compara el hash de la contrasena recibida con el hash almacenado en base de datos.
        return hash(password).equals(passwordHash);
    }
}
