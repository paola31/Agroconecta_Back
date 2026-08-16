import com.agroconecta.pedido.DetallePedidoRepository;
import com.agroconecta.pedido.DireccionCliente;
import com.agroconecta.pedido.DireccionClienteRepository;
import com.agroconecta.pedido.EstadoPedidoRepository;
import com.agroconecta.pedido.MetodoPago;
import com.agroconecta.pedido.MetodoPagoRepository;
import com.agroconecta.pedido.PagoPedidoRepository;
import com.agroconecta.pedido.Pedido;
import com.agroconecta.pedido.PedidoRepository;
import com.agroconecta.pedido.PedidoService;
import com.agroconecta.pedido.dto.PedidoItemRequest;
import com.agroconecta.pedido.dto.PedidoRequest;
import com.agroconecta.pedido.dto.PedidoResponse;
import com.agroconecta.producto.Producto;
import com.agroconecta.producto.ProductoRepository;
import com.agroconecta.stock.Stock;
import com.agroconecta.stock.StockRepository;
import com.agroconecta.usuario.Usuario;
import com.agroconecta.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PedidoServiceTest {
    private UsuarioRepository usuarioRepository;
    private ProductoRepository productoRepository;
    private MetodoPagoRepository metodoPagoRepository;
    private DireccionClienteRepository direccionRepository;
    private PedidoRepository pedidoRepository;
    private DetallePedidoRepository detalleRepository;
    private PagoPedidoRepository pagoRepository;
    private EstadoPedidoRepository estadoRepository;
    private StockRepository stockRepository;
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        productoRepository = mock(ProductoRepository.class);
        metodoPagoRepository = mock(MetodoPagoRepository.class);
        direccionRepository = mock(DireccionClienteRepository.class);
        pedidoRepository = mock(PedidoRepository.class);
        detalleRepository = mock(DetallePedidoRepository.class);
        pagoRepository = mock(PagoPedidoRepository.class);
        estadoRepository = mock(EstadoPedidoRepository.class);
        stockRepository = mock(StockRepository.class);
        pedidoService = new PedidoService(usuarioRepository, productoRepository, metodoPagoRepository,
                direccionRepository, pedidoRepository, detalleRepository, pagoRepository, estadoRepository,
                stockRepository);
    }

    @Test
    void registraPedidoYDescuentaExistencias() {
        prepararDatosBasicos();
        Stock existencia = existencia("10.000");
        when(stockRepository.findByProductoIdOrderByIdAsc(101L)).thenReturn(List.of(existencia));

        PedidoResponse response = pedidoService.crear(request(), 1L);

        assertEquals(5002L, response.getPedidoId());
        assertEquals(new BigDecimal("10000.00"), response.getSubtotal());
        assertEquals(new BigDecimal("18500.00"), response.getTotal());
        assertEquals(new BigDecimal("8.000"), existencia.getCantidad());
        verify(stockRepository).save(existencia);
        verify(detalleRepository).save(any());
        verify(pagoRepository).save(any());
        verify(estadoRepository).save(any());
    }

    @Test
    void rechazaPedidoCuandoNoHayExistenciasSuficientes() {
        prepararDatosBasicos();
        when(stockRepository.findByProductoIdOrderByIdAsc(101L))
                .thenReturn(List.of(existencia("1.000")));

        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class,
                () -> pedidoService.crear(request(), 1L)
        );

        assertEquals("Existencias insuficientes para Ajo. Disponible: 1", error.getMessage());
        verify(pedidoRepository, never()).save(any());
        verify(detalleRepository, never()).save(any());
    }

    private void prepararDatosBasicos() {
        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setRol("cliente");
        cliente.setEstado("activo");

        MetodoPago metodo = new MetodoPago();
        metodo.setId(11L);
        metodo.setNombre("Pago contraentrega");

        Producto ajo = new Producto();
        ajo.setId(101L);
        ajo.setNombre("Ajo");
        ajo.setPrecioUnitario(new BigDecimal("5000.00"));
        ajo.setActivo(true);

        DireccionCliente direccion = new DireccionCliente();
        direccion.setId(2002L);
        Pedido pedido = new Pedido();
        pedido.setId(5002L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(metodoPagoRepository.findByNombreIgnoreCaseAndActivoTrue("Pago contraentrega"))
                .thenReturn(Optional.of(metodo));
        when(productoRepository.findFirstByNombreIgnoreCaseAndActivoTrue("Ajo"))
                .thenReturn(Optional.of(ajo));
        when(direccionRepository.save(any(DireccionCliente.class))).thenReturn(direccion);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
    }

    private Stock existencia(String cantidad) {
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setCantidad(new BigDecimal(cantidad));
        return stock;
    }

    private PedidoRequest request() {
        PedidoItemRequest item = new PedidoItemRequest();
        item.setProductoNombre("Ajo");
        item.setCantidad(2);

        PedidoRequest request = new PedidoRequest();
        request.setClienteId(999L);
        request.setNombreDestinatario("Carlos Ruiz");
        request.setTelefono("3001112222");
        request.setDireccion("Calle 10 # 5-30");
        request.setCiudad("Sesquilé");
        request.setDepartamento("Cundinamarca");
        request.setMetodoPago("contraentrega");
        request.setItems(List.of(item));
        return request;
    }
}
