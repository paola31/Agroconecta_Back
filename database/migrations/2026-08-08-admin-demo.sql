-- Usuario administrador para probar el acceso al panel de Agroconecta.
-- Credenciales del ambiente local: admin@agroconecta.com / AgroAdmin2026

INSERT INTO usuarios (nombre, email, telefono, rol, password_hash, estado)
VALUES ('Administradora Agroconecta', 'admin@agroconecta.com', '3000000000', 'admin', '9d294448bc813783e67632b5f13cd29a8964309ef878f95ac2a4b4e5a6e768d6', 'activo')
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    rol = 'admin',
    password_hash = VALUES(password_hash),
    estado = 'activo';
