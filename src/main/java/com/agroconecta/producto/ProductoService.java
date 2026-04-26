package com.agroconecta.producto;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id " + id));
    }

    @Transactional
    public Producto crear(Producto producto) {
        validarProducto(producto);
        producto.setId(null);
        producto.setActivo(true);

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto datosActualizados) {
        validarProducto(datosActualizados);

        Producto producto = buscarPorId(id);
        producto.setNombre(datosActualizados.getNombre());
        producto.setDescripcion(datosActualizados.getDescripcion());
        producto.setUnidadMedida(datosActualizados.getUnidadMedida());
        producto.setPrecioUnitario(datosActualizados.getPrecioUnitario());
        producto.setImagenUrl(datosActualizados.getImagenUrl());

        return productoRepository.save(producto);
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getUnidadMedida() == null || producto.getUnidadMedida().isBlank()) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria");
        }

        BigDecimal precioUnitario = producto.getPrecioUnitario();
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor o igual a cero");
        }
    }
}
