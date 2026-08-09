package com.agroconecta.pedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "direcciones_cliente")
public class DireccionCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nombre_contacto", nullable = false, length = 120)
    private String nombreContacto;

    @Column(length = 30)
    private String telefono;

    @Column(name = "direccion1", nullable = false, length = 160)
    private String direccion;

    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(nullable = false, length = 80)
    private String departamento;

    @Column(nullable = false, length = 60)
    private String pais;

    @Column(name = "es_principal", nullable = false)
    private Boolean principal = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public void setPais(String pais) { this.pais = pais; }
}
