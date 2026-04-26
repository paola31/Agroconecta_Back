# Agroconecta Back

Backend en Java con Spring Boot para el CRUD de productos del proyecto Agroconecta.

Este repositorio contiene lo necesario para ejecutar el backend y levantar una base MySQL local con Docker.

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
Conexion MySQL OK. Base actual: Agroconecta. Productos registrados: 1
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

- `ProductoController`: expone los endpoints HTTP.
- `ProductoService`: contiene reglas de negocio y validaciones.
- `ProductoRepository`: accede a la base de datos con Spring Data JPA.
- `Producto`: representa la tabla `productos`.
- DTOs: controlan los datos de entrada y salida de la API.

## Nota Sobre JDBC

La guia solicita conexion a base de datos mediante JDBC. En este proyecto se uso Spring Boot porque es un framework de uso real para backend en Java.

La conexion con MySQL se realiza mediante MySQL Connector/J, que es el driver JDBC. Spring Data JPA trabaja sobre esa conexion JDBC para reducir codigo repetitivo en las operaciones de persistencia.

Adicionalmente, la clase `DatabaseConnectionVerifier` usa `JdbcTemplate` para validar al iniciar que el backend puede consultar la base `Agroconecta`, dejando evidencia explicita de conexion JDBC.

## Estandares de Codificacion

El proyecto usa nombres descriptivos para clases, metodos, variables y paquetes:

- Paquete principal: `com.agroconecta`
- Modulo de productos: `com.agroconecta.producto`
- Controlador: `ProductoController`
- Servicio: `ProductoService`
- Repositorio: `ProductoRepository`
- Entidad: `Producto`
- DTOs: `ProductoRequest` y `ProductoResponse`

## Archivos Importantes

```text
pom.xml
docker-compose.yml
database/init/01-agroconecta.sql
src/main/java/com/agroconecta/producto/ProductoController.java
src/main/java/com/agroconecta/producto/ProductoService.java
src/main/java/com/agroconecta/producto/ProductoRepository.java
src/main/java/com/agroconecta/producto/Producto.java
```
