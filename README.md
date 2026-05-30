# Agroconecta Back

Backend en Java con Spring Boot para los modulos de productos e inventario/stock del proyecto Agroconecta.

Este repositorio contiene lo necesario para ejecutar el backend y levantar una base MySQL local con Docker.

## Evidencia actual AA5-EV01

Esta version del repositorio se actualiza para la evidencia `GA7-220501096-AA5-EV01 - diseno y desarrollo de servicios web - caso`. La entrega incorpora una API REST de autenticacion para el proyecto AgroConecta, con servicios de registro e inicio de sesion.

Servicios agregados para esta evidencia:

- `POST /api/auth/register`: registra un usuario nuevo en la base de datos.
- `POST /api/auth/login`: valida correo y contrasena para iniciar sesion.

## Alcance de esta entrega

Esta entrega corresponde a la evidencia `GA7-220501096-AA5-EV01 - diseno y desarrollo de servicios web - caso`.

Se implementa una API REST de autenticacion para el proyecto AgroConecta con dos servicios principales:

- Registro de usuario.
- Inicio de sesion con validacion de correo y contrasena.

La implementacion usa arquitectura por capas:

```text
Controller -> Service -> Repository -> MySQL
```

El backend usa controladores REST, servicios de negocio, repositorios de persistencia, DTOs y entidades JPA.

## Tecnologias

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- MySQL Connector/J
- JdbcTemplate
- Docker Compose

## Requisitos

- Java 17 o superior
- Maven
- Docker
- Docker Compose

Verificar versiones:

```bash
java -version
mvn -version
docker --version
docker compose version
```

## Como Ejecutar el Proyecto

Desde la raiz del repositorio:

```bash
docker compose up -d
mvn spring-boot:run
```

El backend queda disponible en:

```text
http://localhost:8081
```

Al iniciar correctamente debe aparecer en consola una linea similar a:

```text
Conexion MySQL OK. Base actual: Agroconecta. Productos: 1. Registros de stock: 2
```

## Base de Datos

El repositorio incluye un `docker-compose.yml` y un script SQL inicial para que la base pueda levantarse en otro equipo.

```text
database/init/01-agroconecta.sql
```

El contenedor crea:

```text
Base de datos: Agroconecta
Usuario: agro_backend
Password: agro_backend123
Puerto: 3306
```

La aplicacion usa esos valores como configuracion local por defecto. Si se requiere cambiar la conexion sin modificar el codigo, se pueden definir variables de entorno:

```bash
export DB_URL="jdbc:mysql://localhost:3306/Agroconecta?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true"
export DB_USER="agro_backend"
export DB_PASSWORD="agro_backend123"
```

Levantar MySQL:

```bash
docker compose up -d
```

Si ya existe un volumen anterior y se quiere reiniciar la base desde cero:

```bash
docker compose down -v
docker compose up -d
```

## Ejecutar Solo el Backend

```bash
mvn spring-boot:run
```

El backend queda disponible en:

```text
http://localhost:8081
```

Si el puerto `8081` esta ocupado, se puede ejecutar temporalmente en otro puerto:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```

## Endpoints CRUD

### Productos

Listar productos activos:

```bash
curl http://localhost:8081/api/productos
```

Consultar producto por id:

```bash
curl http://localhost:8081/api/productos/100
```

Crear producto:

```bash
curl -X POST http://localhost:8081/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Tomate",
    "descripcion": "Tomate fresco seleccionado",
    "unidadMedida": "kg",
    "precioUnitario": 4300,
    "imagenUrl": "https://example.com/tomate.jpg"
  }'
```

Actualizar producto:

```bash
curl -X PUT http://localhost:8081/api/productos/100 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Papa Pastusa Premium",
    "descripcion": "Papa fresca seleccionada",
    "unidadMedida": "kg",
    "precioUnitario": 2800,
    "imagenUrl": "https://imgs.example.com/papa.jpg"
  }'
```

Eliminar/desactivar producto:

```bash
curl -X DELETE http://localhost:8081/api/productos/100
```

La eliminacion se implementa como borrado logico. El registro no se borra fisicamente de MySQL; se actualiza el campo `activo` a `false`. Esto conserva historial e integridad con tablas relacionadas como `stock`, `detalle_pedidos` y `movimientos_stock`.

Despues de desactivar un producto, este deja de aparecer en:

```bash
curl http://localhost:8081/api/productos
```

porque el listado principal solo muestra productos activos.

### Stock

Listar registros de stock:

```bash
curl http://localhost:8081/api/stock
```

Consultar stock por id:

```bash
curl http://localhost:8081/api/stock/1
```

Crear stock:

```bash
curl -X POST http://localhost:8081/api/stock \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "productoId": 100,
    "cantidad": 8.500
  }'
```

Actualizar stock:

```bash
curl -X PUT http://localhost:8081/api/stock/1 \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 2,
    "productoId": 100,
    "cantidad": 15.000
  }'
```

Eliminar stock:

```bash
curl -X DELETE http://localhost:8081/api/stock/1
```

## Verificacion de la Evidencia AA5-EV01

Para validar esta evidencia se prepararon los siguientes casos de prueba para Postman o curl:

- `POST /api/auth/register`: registro correcto de usuario.
- `POST /api/auth/register`: validacion de correo repetido.
- `POST /api/auth/login`: inicio de sesion correcto.
- `POST /api/auth/login`: error con contrasena incorrecta.
- `POST /api/auth/login`: error con correo no registrado.
- Validacion de campos obligatorios en registro e inicio de sesion.

En este equipo no se ejecuto `mvn test` porque Maven no esta instalado y el proyecto no incluye Maven Wrapper (`mvnw`). La prueba funcional debe realizarse levantando MySQL con Docker, ejecutando el backend con Maven y probando los endpoints documentados en `ENDPOINTS_AA5_EV01.md`.

## Relacion con el Frontend

El frontend esta en un repositorio separado. Para probar la integracion desde la interfaz, ejecutar este backend en `http://localhost:8081` y luego levantar el frontend.

En el frontend, la pantalla de prueba del CRUD es:

```text
/admin/inventory
```

Desde esa pantalla se puede listar, crear, consultar, actualizar y desactivar productos.

## Estructura

```text
Controller -> Service -> Repository -> MySQL
```

- `ProductoController` y `StockController`: exponen los endpoints HTTP.
- `ProductoService` y `StockService`: contienen reglas de negocio y validaciones.
- `ProductoRepository` y `StockRepository`: acceden a la base de datos con Spring Data JPA.
- `Producto` y `Stock`: representan las tablas `productos` y `stock`.
- DTOs: controlan los datos de entrada y salida de la API.

## Nota Sobre JDBC

La guia solicita conexion a base de datos mediante JDBC. En este proyecto se uso Spring Boot porque es un framework de uso real para backend en Java.

La conexion con MySQL se realiza mediante MySQL Connector/J, que es el driver JDBC. Spring Data JPA trabaja sobre esa conexion JDBC para reducir codigo repetitivo en las operaciones de persistencia.

Adicionalmente, la clase `DatabaseConnectionVerifier` usa `JdbcTemplate` para validar al iniciar que el backend puede consultar la base `Agroconecta`, dejando evidencia explicita de conexion JDBC.

## Estandares de Codificacion

El proyecto usa nombres descriptivos para clases, metodos, variables y paquetes:

- Paquete principal: `com.agroconecta`
- Modulo de productos: `com.agroconecta.producto`
- Modulo de stock: `com.agroconecta.stock`
- Controlador: `ProductoController`
- Servicio: `ProductoService`
- Repositorio: `ProductoRepository`
- Entidad: `Producto`
- DTOs: `ProductoRequest`, `ProductoResponse`, `StockRequest` y `StockResponse`

## Archivos Importantes

```text
pom.xml
docker-compose.yml
database/init/01-agroconecta.sql
src/main/java/com/agroconecta/producto/ProductoController.java
src/main/java/com/agroconecta/producto/ProductoService.java
src/main/java/com/agroconecta/producto/ProductoRepository.java
src/main/java/com/agroconecta/producto/Producto.java
src/main/java/com/agroconecta/stock/StockController.java
src/main/java/com/agroconecta/stock/StockService.java
src/main/java/com/agroconecta/stock/StockRepository.java
src/main/java/com/agroconecta/stock/Stock.java
```

## Evidencia AA5-EV01 - Servicios Web de Registro e Inicio de Sesion

Para la evidencia `GA7-220501096-AA5-EV01 - diseno y desarrollo de servicios web - caso` se agrego una API REST de autenticacion basica.

Endpoints implementados:

```text
POST /api/auth/register
POST /api/auth/login
```

El registro crea usuarios en la tabla `usuarios` y guarda la contrasena como hash SHA-256 en el campo `password_hash`. El inicio de sesion valida correo, contrasena y estado activo del usuario.

Ejemplo de registro:

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana Cliente",
    "email": "ana.cliente@example.com",
    "telefono": "3005556677",
    "rol": "cliente",
    "password": "123456"
  }'
```

Ejemplo de inicio de sesion correcto:

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ana.cliente@example.com",
    "password": "123456"
  }'
```

Respuesta esperada:

```json
{
  "mensaje": "Autenticacion satisfactoria",
  "usuarioId": 4,
  "nombre": "Ana Cliente",
  "email": "ana.cliente@example.com",
  "rol": "cliente"
}
```

Ejemplo de inicio de sesion incorrecto:

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ana.cliente@example.com",
    "password": "claveIncorrecta"
  }'
```

Respuesta esperada: `401 Unauthorized` con mensaje `Error en la autenticacion`.

Si ya existe una base creada antes de esta evidencia, ejecutar primero:

```text
database/migrations/2026-05-30-aa5-auth.sql
```
