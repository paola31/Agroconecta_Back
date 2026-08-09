package com.agroconecta.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "direccion_envio_id")
    private Long direccionEnvioId;

    @Column(name = "estado_actual_id", nullable = false, length = 10)
    private String estadoActualId;

    @Column(name = "id_metodo_pago", nullable = false)
    private Long metodoPagoId;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public void setDireccionEnvioId(Long direccionEnvioId) { this.direccionEnvioId = direccionEnvioId; }
    public void setEstadoActualId(String estadoActualId) { this.estadoActualId = estadoActualId; }
    public void setMetodoPagoId(Long metodoPagoId) { this.metodoPagoId = metodoPagoId; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
