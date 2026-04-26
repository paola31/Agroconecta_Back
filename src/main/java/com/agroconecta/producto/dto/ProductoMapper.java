package com.agroconecta.producto.dto;

import com.agroconecta.producto.Producto;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setPrecioUnitario(request.getPrecioUnitario());
        producto.setImagenUrl(request.getImagenUrl());
        return producto;
    }
}
