package com.agroconecta.pedido.dto;

import java.math.BigDecimal;

public class PedidoResponse {
    private final Long pedidoId;
    private final String estado;
    private final BigDecimal subtotal;
    private final BigDecimal envio;
    private final BigDecimal total;
    private final String mensaje;

    public PedidoResponse(Long pedidoId, String estado, BigDecimal subtotal, BigDecimal envio, BigDecimal total, String mensaje) {
        this.pedidoId = pedidoId;
        this.estado = estado;
        this.subtotal = subtotal;
        this.envio = envio;
        this.total = total;
        this.mensaje = mensaje;
    }

    public Long getPedidoId() { return pedidoId; }
    public String getEstado() { return estado; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getEnvio() { return envio; }
    public BigDecimal getTotal() { return total; }
    public String getMensaje() { return mensaje; }
}
