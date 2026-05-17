# Agroconecta Back

Backend en Java con Spring Boot para los modulos de productos e inventario/stock del proyecto Agroconecta.

Este repositorio contiene lo necesario para ejecutar el backend y levantar una base MySQL local con Docker.

## Evidencia AA3-EV01

Este proyecto se presenta para la evidencia `GA7-220501096-AA3-EV01 - codificacion de modulos del software Stand alone, web y movil de acuerdo al proyecto a desarrollar`.

Para esta evidencia se selecciona el desarrollo web de los modulos de productos e inventario/stock del proyecto Agroconecta. La implementacion se realiza con Spring Boot como framework Java, exponiendo una API REST conectada a MySQL.

Alcance del modulo:

- Consultar productos activos.
- Consultar producto por identificador.
- Registrar productos.
- Actualizar productos.
- Desactivar productos mediante borrado logico.
- Consultar registros de stock.
- Registrar cantidades de stock por usuario y producto.
- Actualizar cantidades de stock.
- Eliminar registros de stock.

Artefactos previos considerados:

- `GA4-220501095-AA2-EV02`: informe de entregables y requisitos funcionales del proyecto. Se toman como referencia `RF2 - Publicacion de productos`, `RF3 - Busqueda de productos` y `RF5 - Gestion de disponibilidad de productos`.
- `GA4-220501095-AA2-EV04`: diagrama de clases del proyecto, donde se identifica `Producto` como una clase principal del dominio.
- `GA4-220501095-AA2-EV05`: arquitectura de software. Aunque en la planeacion se contemplo Laravel/MVC, para esta evidencia se ajusta la tecnologia a Spring Boot porque la actividad solicita aplicar frameworks de Java.
- `GA4-220501095-AA4-EV04`: evaluacion de artefactos de diseno, usada como referencia para mantener trazabilidad entre requisitos, diseno e implementacion.

Arquitectura aplicada:

```text
Controller -> Service -> Repository -> MySQL
```

El backend usa arquitectura por capas con controladores REST, servicios de negocio, repositorios de persistencia, DTOs y entidades JPA.

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

## Verificacion Realizada

Para validar la implementacion se ejecutaron las siguientes verificaciones:

```bash
mvn package
```

Resultado: compilacion correcta del proyecto.

Tambien se probaron endpoints HTTP del modulo de stock:

- `GET /api/stock`: consulta general de stock.
- `GET /api/stock/1`: consulta por identificador.
- `POST /api/stock`: creacion de registro temporal.
- `PUT /api/stock/{id}`: actualizacion del registro temporal.
- `DELETE /api/stock/{id}`: eliminacion del registro temporal.
- `POST /api/stock` con cantidad negativa: validacion de datos de entrada.

Durante la prueba se creo un registro temporal de stock, se actualizo y posteriormente se elimino para conservar limpia la base de datos.

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
