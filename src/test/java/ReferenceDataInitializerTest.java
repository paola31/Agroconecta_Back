import com.agroconecta.config.ReferenceDataInitializer;
import com.agroconecta.pedido.MetodoPagoRepository;
import com.agroconecta.producto.Producto;
import com.agroconecta.producto.ProductoRepository;
import com.agroconecta.stock.Stock;
import com.agroconecta.stock.StockRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReferenceDataInitializerTest {

    @Test
    void creaStockInicialSoloParaProductosSinExistencias() throws Exception {
        MetodoPagoRepository metodoPagoRepository = mock(MetodoPagoRepository.class);
        ProductoRepository productoRepository = mock(ProductoRepository.class);
        StockRepository stockRepository = mock(StockRepository.class);
        Producto tomate = producto(103L, "Tomate");
        Producto papa = producto(100L, "Papa Pastusa");

        when(productoRepository.findFirstByNombreIgnoreCaseAndActivoTrue(anyString()))
                .thenReturn(Optional.of(tomate));
        when(productoRepository.findByActivoTrueOrderByNombreAsc()).thenReturn(List.of(tomate, papa));
        when(stockRepository.existsByProductoId(103L)).thenReturn(false);
        when(stockRepository.existsByProductoId(100L)).thenReturn(true);

        new ReferenceDataInitializer(metodoPagoRepository, productoRepository, stockRepository).run(null);

        ArgumentCaptor<Stock> captor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(captor.capture());
        Stock creado = captor.getValue();
        assertEquals(2L, creado.getUsuarioId());
        assertSame(tomate, creado.getProducto());
        assertEquals(new BigDecimal("20.000"), creado.getCantidad());
        verify(stockRepository, never()).findByUsuarioIdAndProductoId(2L, 100L);
    }

    private Producto producto(Long id, String nombre) {
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);
        return producto;
    }
}
