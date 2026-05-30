# Endpoints GA7-220501096-AA5-EV01

Base local:

```text
http://localhost:8081
```

## Registrar usuario

```http
POST /api/auth/register
Content-Type: application/json
```

Body:

```json
{
  "nombre": "Ana Cliente",
  "email": "ana.cliente@example.com",
  "telefono": "3005556677",
  "rol": "cliente",
  "password": "123456"
}
```

Respuesta esperada: `201 Created`.

```json
{
  "mensaje": "Usuario registrado correctamente",
  "usuarioId": 4,
  "nombre": "Ana Cliente",
  "email": "ana.cliente@example.com",
  "rol": "cliente"
}
```

## Iniciar sesion correctamente

```http
POST /api/auth/login
Content-Type: application/json
```

Body:

```json
{
  "email": "ana.cliente@example.com",
  "password": "123456"
}
```

Respuesta esperada: `200 OK`.

```json
{
  "mensaje": "Autenticacion satisfactoria",
  "usuarioId": 4,
  "nombre": "Ana Cliente",
  "email": "ana.cliente@example.com",
  "rol": "cliente"
}
```

## Iniciar sesion con datos incorrectos

```http
POST /api/auth/login
Content-Type: application/json
```

Body:

```json
{
  "email": "ana.cliente@example.com",
  "password": "claveIncorrecta"
}
```

Respuesta esperada: `401 Unauthorized`.

```json
{
  "status": 401,
  "error": "Autenticacion fallida",
  "messages": [
    "Error en la autenticacion"
  ]
}
```

## Usuarios de prueba incluidos en la base inicial

Los usuarios precargados pueden iniciar sesion con la contrasena `123456`:

- `carlos@example.com`
- `juana@example.com`
- `mario@example.com`
