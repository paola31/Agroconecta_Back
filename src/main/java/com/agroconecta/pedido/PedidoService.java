package com.agroconecta.pedido;

import com.agroconecta.pedido.dto.PedidoItemRequest;
import com.agroconecta.pedido.dto.PedidoRequest;
import com.agroconecta.pedido.dto.PedidoResponse;
import com.agroconecta.producto.Producto;
import com.agroconecta.producto.ProductoRepository;
import com.agroconecta.usuario.Usuario;
import com.agroconecta.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private static final BigDecimal COSTO_ENVIO = new BigDecimal("8500.00");
    private static final String ESTADO_PENDIENTE = "PEN";

    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final DireccionClienteRepository direccionRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detalleRepository;
    private final PagoPedidoRepository pagoRepository;
    private final EstadoPedidoRepository estadoRepository;

    public PedidoService(UsuarioRepository usuarioRepository, ProductoRepository productoRepository,
                         MetodoPagoRepository metodoPagoRepository, DireccionClienteRepository direccionRepository,
                         PedidoRepository pedidoRepository, DetallePedidoRepository detalleRepository,
                         PagoPedidoRepository pagoRepository, EstadoPedidoRepository estadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.direccionRepository = direccionRepository;
        this.pedidoRepository = pedidoRepository;
        this.detalleRepository = detalleRepository;
        this.pagoRepository = pagoRepository;
        this.estadoRepository = estadoRepository;
    }

    @Transactional
    public PedidoResponse crear(PedidoRequest request) {
        Usuario cliente = usuarioRepository.findById(request.getClienteId())
                .filter(usuario -> "activo".equalsIgnoreCase(usuario.getEstado()))
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe o está inactivo"));

        if ("admin".equalsIgnoreCase(cliente.getRol())) {
            throw new IllegalArgumentException("Un administrador no puede registrar pedidos como cliente");
        }

        String nombreMetodo = "contraentrega".equals(request.getMetodoPago())
                ? "Pago contraentrega" : "Transferencia Bancaria";
        MetodoPago metodoPago = metodoPagoRepository.findByNombreIgnoreCaseAndActivoTrue(nombreMetodo)
                .orElseThrow(() -> new IllegalArgumentException("El método de pago no está disponible"));

        List<LineaCalculada> lineas = new ArrayList<>();
        BigDecimal subtotalPedido = BigDecimal.ZERO;
        for (PedidoItemRequest item : request.getItems()) {
            Producto producto = productoRepository.findFirstByNombreIgnoreCaseAndActivoTrue(item.getProductoNombre().trim())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El producto " + item.getProductoNombre() + " no está disponible"));
            BigDecimal cantidad = BigDecimal.valueOf(item.getCantidad());
            BigDecimal subtotal = producto.getPrecioUnitario().multiply(cantidad);
            lineas.add(new LineaCalculada(producto, cantidad, subtotal));
            subtotalPedido = subtotalPedido.add(subtotal);
        }

        BigDecimal total = subtotalPedido.add(COSTO_ENVIO);
        DireccionCliente direccion = new DireccionCliente();
        direccion.setUsuarioId(cliente.getId());
        direccion.setNombreContacto(request.getNombreDestinatario().trim());
        direccion.setTelefono(request.getTelefono());
        direccion.setDireccion(request.getDireccion().trim());
        direccion.setCiudad(request.getCiudad().trim());
        direccion.setDepartamento(request.getDepartamento() == null || request.getDepartamento().isBlank()
                ? "Cundinamarca" : request.getDepartamento().trim());
        direccion.setPais("Colombia");
        direccion = direccionRepository.save(direccion);

        Pedido pedido = new Pedido();
        pedido.setClienteId(cliente.getId());
        pedido.setDireccionEnvioId(direccion.getId());
        pedido.setEstadoActualId(ESTADO_PENDIENTE);
        pedido.setMetodoPagoId(metodoPago.getId());
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        for (LineaCalculada linea : lineas) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedidoId(pedido.getId());
            detalle.setProductoId(linea.producto().getId());
            detalle.setCantidad(linea.cantidad());
            detalle.setPrecioUnitario(linea.producto().getPrecioUnitario());
            detalle.setSubtotal(linea.subtotal());
            detalleRepository.save(detalle);
        }

        PagoPedido pago = new PagoPedido();
        pago.setPedidoId(pedido.getId());
        pago.setMetodoPagoId(metodoPago.getId());
        pago.setEstado("pendiente");
        pago.setMonto(total);
        pagoRepository.save(pago);

        EstadoPedido estado = new EstadoPedido();
        estado.setPedidoId(pedido.getId());
        estado.setEstadoId(ESTADO_PENDIENTE);
        estado.setComentario("Pedido registrado desde el carrito web");
        estado.setCambiadoPor(cliente.getId());
        estadoRepository.save(estado);

        return new PedidoResponse(pedido.getId(), "Pendiente", subtotalPedido, COSTO_ENVIO, total,
                "Pedido registrado correctamente");
    }

    private record LineaCalculada(Producto producto, BigDecimal cantidad, BigDecimal subtotal) {
    }
}
