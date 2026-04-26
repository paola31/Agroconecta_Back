package com.agroconecta.producto;

import com.agroconecta.producto.dto.ProductoMapper;
import com.agroconecta.producto.dto.ProductoRequest;
import com.agroconecta.producto.dto.ProductoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoResponse> listarActivos() {
        return productoService.listarActivos()
                .stream()
                .map(ProductoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductoResponse buscarPorId(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id);
        return ProductoResponse.fromEntity(producto);
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        Producto productoCreado = productoService.crear(ProductoMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.fromEntity(productoCreado));
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        Producto productoActualizado = productoService.actualizar(id, ProductoMapper.toEntity(request));
        return ProductoResponse.fromEntity(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        productoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
