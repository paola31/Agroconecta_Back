package com.agroconecta.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "pago_pedido")
public class PagoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "metodo_pago_id", nullable = false)
    private Long metodoPagoId;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(precision = 14, scale = 2, nullable = false)
    private BigDecimal monto;

    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public void setMetodoPagoId(Long metodoPagoId) { this.metodoPagoId = metodoPagoId; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}
