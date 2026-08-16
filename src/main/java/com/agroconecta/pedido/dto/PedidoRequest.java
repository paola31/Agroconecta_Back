package com.agroconecta.pedido.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PedidoRequest {
    private Long clienteId;

    @NotBlank(message = "El nombre del destinatario es obligatorio")
    @Size(max = 120)
    private String nombreDestinatario;

    @Size(max = 30)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 160)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80)
    private String ciudad;

    @Size(max = 80)
    private String departamento;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "contraentrega|transferencia", message = "El método de pago no es válido")
    private String metodoPago;

    @NotEmpty(message = "El pedido debe incluir al menos un producto")
    private List<@Valid PedidoItemRequest> items;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getNombreDestinatario() { return nombreDestinatario; }
    public void setNombreDestinatario(String nombreDestinatario) { this.nombreDestinatario = nombreDestinatario; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public List<PedidoItemRequest> getItems() { return items; }
    public void setItems(List<PedidoItemRequest> items) { this.items = items; }
}
