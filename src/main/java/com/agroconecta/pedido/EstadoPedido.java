package com.agroconecta.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estado_pedidos")
public class EstadoPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "estado_id", nullable = false, length = 10)
    private String estadoId;

    @Column(length = 255)
    private String comentario;

    @Column(name = "cambiado_por")
    private Long cambiadoPor;

    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public void setEstadoId(String estadoId) { this.estadoId = estadoId; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setCambiadoPor(Long cambiadoPor) { this.cambiadoPor = cambiadoPor; }
}
