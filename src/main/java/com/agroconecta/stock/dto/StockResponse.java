package com.agroconecta.stock.dto;

import com.agroconecta.stock.Stock;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class StockResponse {

    private Long id;
    private Long usuarioId;
    private Long productoId;
    private String productoNombre;
    private BigDecimal cantidad;
    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    public static StockResponse fromEntity(Stock stock) {
        StockResponse response = new StockResponse();
        response.setId(stock.getId());
        response.setUsuarioId(stock.getUsuarioId());
        response.setProductoId(stock.getProducto().getId());
        response.setProductoNombre(stock.getProducto().getNombre());
        response.setCantidad(stock.getCantidad());
        response.setCreadoEn(stock.getCreadoEn());
        response.setActualizadoEn(stock.getActualizadoEn());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(LocalDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }
}
