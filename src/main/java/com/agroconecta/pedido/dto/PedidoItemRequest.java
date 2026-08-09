package com.agroconecta.pedido.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class PedidoItemRequest {
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String productoNombre;

    @Min(value = 1, message = "La cantidad debe ser mayor que cero")
    @Max(value = 100, message = "La cantidad no debe superar 100 unidades")
    private int cantidad;

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
