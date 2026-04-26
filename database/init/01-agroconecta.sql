-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: localhost    Database: Agroconecta
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `Agroconecta`
--

/*!40000 DROP DATABASE IF EXISTS `Agroconecta`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `Agroconecta` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `Agroconecta`;

--
-- Table structure for table `asignaciones_stock_detalle`
--

DROP TABLE IF EXISTS `asignaciones_stock_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignaciones_stock_detalle` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `detalle_pedido_id` bigint unsigned NOT NULL,
  `producto_id` bigint unsigned NOT NULL,
  `productor_id` bigint unsigned NOT NULL,
  `cantidad_asignada` decimal(12,3) NOT NULL,
  `precio_unitario` decimal(12,2) NOT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_asd_detalle` (`detalle_pedido_id`),
  KEY `idx_asd_productor` (`productor_id`),
  KEY `idx_asd_producto` (`producto_id`),
  CONSTRAINT `fk_asd_detalle` FOREIGN KEY (`detalle_pedido_id`) REFERENCES `detalle_pedidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asd_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_asd_productor` FOREIGN KEY (`productor_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `asignaciones_stock_detalle_chk_1` CHECK ((`cantidad_asignada` > 0)),
  CONSTRAINT `asignaciones_stock_detalle_chk_2` CHECK ((`precio_unitario` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=9103 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignaciones_stock_detalle`
--

LOCK TABLES `asignaciones_stock_detalle` WRITE;
/*!40000 ALTER TABLE `asignaciones_stock_detalle` DISABLE KEYS */;
INSERT INTO `asignaciones_stock_detalle` VALUES (9101,7001,100,2,10.000,2500.00,'2025-08-24 21:15:50'),(9102,7001,100,3,5.000,2500.00,'2025-08-24 21:15:50');
/*!40000 ALTER TABLE `asignaciones_stock_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentarios_calificacion`
--

DROP TABLE IF EXISTS `comentarios_calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentarios_calificacion` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint unsigned NOT NULL,
  `cliente_id` bigint unsigned NOT NULL,
  `calificacion` tinyint unsigned NOT NULL,
  `comentario` varchar(500) DEFAULT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cc_pedido_cliente` (`pedido_id`,`cliente_id`),
  KEY `idx_cc_cliente` (`cliente_id`),
  CONSTRAINT `fk_cc_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cc_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comentarios_calificacion_chk_1` CHECK ((`calificacion` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=6002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentarios_calificacion`
--

LOCK TABLES `comentarios_calificacion` WRITE;
/*!40000 ALTER TABLE `comentarios_calificacion` DISABLE KEYS */;
INSERT INTO `comentarios_calificacion` VALUES (6001,5001,1,5,'Excelente calidad y entrega a tiempo.','2025-08-24 21:15:58','2025-08-24 21:15:58');
/*!40000 ALTER TABLE `comentarios_calificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_pedidos`
--

DROP TABLE IF EXISTS `detalle_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_pedidos` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint unsigned NOT NULL,
  `producto_id` bigint unsigned NOT NULL,
  `cantidad` decimal(12,3) NOT NULL,
  `precio_unitario` decimal(12,2) NOT NULL,
  `subtotal` decimal(14,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_dp_pedido` (`pedido_id`),
  KEY `idx_dp_producto` (`producto_id`),
  CONSTRAINT `fk_dp_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_dp_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `detalle_pedidos_chk_1` CHECK ((`cantidad` > 0)),
  CONSTRAINT `detalle_pedidos_chk_2` CHECK ((`precio_unitario` >= 0)),
  CONSTRAINT `detalle_pedidos_chk_3` CHECK ((`subtotal` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=7002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_pedidos`
--

LOCK TABLES `detalle_pedidos` WRITE;
/*!40000 ALTER TABLE `detalle_pedidos` DISABLE KEYS */;
INSERT INTO `detalle_pedidos` VALUES (7001,5001,100,15.000,2500.00,37500.00);
/*!40000 ALTER TABLE `detalle_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `direcciones_cliente`
--

DROP TABLE IF EXISTS `direcciones_cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `direcciones_cliente` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint unsigned NOT NULL,
  `nombre_contacto` varchar(120) NOT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `direccion1` varchar(160) NOT NULL,
  `direccion2` varchar(160) DEFAULT NULL,
  `ciudad` varchar(80) NOT NULL,
  `departamento` varchar(80) NOT NULL,
  `codigo_postal` varchar(20) DEFAULT NULL,
  `pais` varchar(60) NOT NULL,
  `es_principal` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_dir_usuario` (`usuario_id`),
  CONSTRAINT `fk_dir_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=2002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `direcciones_cliente`
--

LOCK TABLES `direcciones_cliente` WRITE;
/*!40000 ALTER TABLE `direcciones_cliente` DISABLE KEYS */;
INSERT INTO `direcciones_cliente` VALUES (2001,1,'Carlos Ruiz','3001112222','Calle 10 # 5-30',NULL,'Sesquilé','Cundinamarca',NULL,'Colombia',1);
/*!40000 ALTER TABLE `direcciones_cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_pedidos`
--

DROP TABLE IF EXISTS `estado_pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_pedidos` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint unsigned NOT NULL,
  `estado_id` varchar(10) NOT NULL,
  `comentario` varchar(255) DEFAULT NULL,
  `cambiado_por` bigint unsigned DEFAULT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ep_pedido` (`pedido_id`),
  KEY `idx_ep_estado` (`estado_id`),
  KEY `idx_ep_cambiado_por` (`cambiado_por`),
  CONSTRAINT `fk_ep_estado` FOREIGN KEY (`estado_id`) REFERENCES `estados_de_pedido` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ep_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ep_usuario` FOREIGN KEY (`cambiado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_pedidos`
--

LOCK TABLES `estado_pedidos` WRITE;
/*!40000 ALTER TABLE `estado_pedidos` DISABLE KEYS */;
INSERT INTO `estado_pedidos` VALUES (9001,5001,'PEN','Pedido creado y pendiente de pago',1,'2025-08-24 21:15:39');
/*!40000 ALTER TABLE `estado_pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estados_de_pedido`
--

DROP TABLE IF EXISTS `estados_de_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estados_de_pedido` (
  `id` varchar(10) NOT NULL,
  `nombre` varchar(60) NOT NULL,
  `orden` int NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_estados_nombre` (`nombre`),
  KEY `idx_estados_activo` (`activo`),
  KEY `idx_estados_orden` (`orden`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estados_de_pedido`
--

LOCK TABLES `estados_de_pedido` WRITE;
/*!40000 ALTER TABLE `estados_de_pedido` DISABLE KEYS */;
INSERT INTO `estados_de_pedido` VALUES ('PEN','Pendiente',1,1);
/*!40000 ALTER TABLE `estados_de_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metodos_de_pago`
--

DROP TABLE IF EXISTS `metodos_de_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `metodos_de_pago` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(60) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_metodos_nombre` (`nombre`),
  KEY `idx_metodos_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metodos_de_pago`
--

LOCK TABLES `metodos_de_pago` WRITE;
/*!40000 ALTER TABLE `metodos_de_pago` DISABLE KEYS */;
INSERT INTO `metodos_de_pago` VALUES (10,'Transferencia Bancaria',1,'2025-08-24 21:15:20','2025-08-24 21:15:20');
/*!40000 ALTER TABLE `metodos_de_pago` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movimientos_stock`
--

DROP TABLE IF EXISTS `movimientos_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movimientos_stock` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint unsigned NOT NULL,
  `producto_id` bigint unsigned NOT NULL,
  `tipo` enum('ingreso','egreso','ajuste') NOT NULL,
  `pedido_id` bigint unsigned DEFAULT NULL,
  `detalle_pedido_id` bigint unsigned DEFAULT NULL,
  `cantidad` decimal(12,3) NOT NULL,
  `nota` varchar(255) DEFAULT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ms_usuario` (`usuario_id`),
  KEY `idx_ms_producto` (`producto_id`),
  KEY `idx_ms_pedido` (`pedido_id`),
  KEY `idx_ms_detalle` (`detalle_pedido_id`),
  CONSTRAINT `fk_ms_detalle` FOREIGN KEY (`detalle_pedido_id`) REFERENCES `detalle_pedidos` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_ms_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_ms_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ms_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9303 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movimientos_stock`
--

LOCK TABLES `movimientos_stock` WRITE;
/*!40000 ALTER TABLE `movimientos_stock` DISABLE KEYS */;
INSERT INTO `movimientos_stock` VALUES (9301,2,100,'egreso',5001,7001,-10.000,'Venta pedido 5001','2025-08-24 21:15:55'),(9302,3,100,'egreso',5001,7001,-5.000,'Venta pedido 5001','2025-08-24 21:15:55');
/*!40000 ALTER TABLE `movimientos_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago_pedido`
--

DROP TABLE IF EXISTS `pago_pedido`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago_pedido` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint unsigned NOT NULL,
  `metodo_pago_id` bigint unsigned NOT NULL,
  `estado` enum('pendiente','aprobado','rechazado','reembolsado') NOT NULL,
  `referencia` varchar(120) DEFAULT NULL,
  `monto` decimal(14,2) NOT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pp_pedido` (`pedido_id`),
  KEY `idx_pp_metodo` (`metodo_pago_id`),
  KEY `idx_pp_estado` (`estado`),
  CONSTRAINT `fk_pp_metodo` FOREIGN KEY (`metodo_pago_id`) REFERENCES `metodos_de_pago` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pp_pedido` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=8002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago_pedido`
--

LOCK TABLES `pago_pedido` WRITE;
/*!40000 ALTER TABLE `pago_pedido` DISABLE KEYS */;
INSERT INTO `pago_pedido` VALUES (8001,5001,10,'pendiente','REF-XYZ-123',75000.00,'2025-08-24 21:15:34','2025-08-24 21:15:34');
/*!40000 ALTER TABLE `pago_pedido` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint unsigned NOT NULL,
  `direccion_envio_id` bigint unsigned DEFAULT NULL,
  `estado_actual_id` varchar(10) NOT NULL,
  `id_metodo_pago` bigint unsigned NOT NULL,
  `total` decimal(14,2) NOT NULL DEFAULT '0.00',
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_pedidos_cliente` (`cliente_id`),
  KEY `idx_pedidos_estado_actual` (`estado_actual_id`),
  KEY `idx_pedidos_metodo` (`id_metodo_pago`),
  KEY `idx_pedidos_direccion` (`direccion_envio_id`),
  CONSTRAINT `fk_pedidos_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pedidos_direccion` FOREIGN KEY (`direccion_envio_id`) REFERENCES `direcciones_cliente` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pedidos_estado` FOREIGN KEY (`estado_actual_id`) REFERENCES `estados_de_pedido` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pedidos_metodo` FOREIGN KEY (`id_metodo_pago`) REFERENCES `metodos_de_pago` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=5002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos`
--

LOCK TABLES `pedidos` WRITE;
/*!40000 ALTER TABLE `pedidos` DISABLE KEYS */;
INSERT INTO `pedidos` VALUES (5001,1,2001,'PEN',10,75000.00,'2025-08-24 21:15:30','2025-08-24 21:15:30');
/*!40000 ALTER TABLE `pedidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(140) NOT NULL,
  `descripcion` text,
  `unidad_medida` varchar(20) NOT NULL,
  `precio_unitario` decimal(12,2) NOT NULL,
  `imagen_url` varchar(255) DEFAULT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_productos_activo` (`activo`),
  KEY `idx_productos_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (100,'Papa Pastusa','Papa fresca seleccionada','kg',2500.00,'https://imgs.example.com/papa.jpg','2025-08-24 21:15:14','2025-08-24 21:15:14',1);
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint unsigned NOT NULL,
  `producto_id` bigint unsigned NOT NULL,
  `cantidad` decimal(12,3) NOT NULL,
  `creado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_stock_usuario_producto` (`usuario_id`,`producto_id`),
  KEY `idx_stock_producto` (`producto_id`),
  CONSTRAINT `fk_stock_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_stock_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `stock_chk_1` CHECK ((`cantidad` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock`
--

LOCK TABLES `stock` WRITE;
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` VALUES (1,2,100,10.000,'2025-08-24 21:15:44','2025-08-24 21:15:44'),(2,3,100,10.000,'2025-08-24 21:15:44','2025-08-24 21:15:44');
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(120) NOT NULL,
  `email` varchar(160) NOT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `rol` enum('campesino','cliente','admin') NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `estado` enum('activo','inactivo') NOT NULL DEFAULT 'activo',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_usuarios_email` (`email`),
  KEY `idx_usuarios_rol` (`rol`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Carlos Ruiz','carlos@example.com','3001112222','cliente','2025-08-24 21:15:09','2025-08-24 21:15:09','activo'),(2,'Juana Campesina','juana@example.com','3002223333','campesino','2025-08-24 21:15:09','2025-08-24 21:15:09','activo'),(3,'Mario Productor','mario@example.com','3003334444','campesino','2025-08-24 21:15:09','2025-08-24 21:15:09','activo');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'Agroconecta'
--

--
-- Dumping routines for database 'Agroconecta'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-26  2:33:40
