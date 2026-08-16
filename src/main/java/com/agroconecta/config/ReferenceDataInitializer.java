package com.agroconecta.config;

import com.agroconecta.pedido.MetodoPago;
import com.agroconecta.pedido.MetodoPagoRepository;
import com.agroconecta.producto.Producto;
import com.agroconecta.producto.ProductoRepository;
import com.agroconecta.stock.Stock;
import com.agroconecta.stock.StockRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ReferenceDataInitializer implements ApplicationRunner {
    private final MetodoPagoRepository metodoPagoRepository;
    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;

    public ReferenceDataInitializer(MetodoPagoRepository metodoPagoRepository,
                                    ProductoRepository productoRepository,
                                    StockRepository stockRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoRepository = productoRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        crearMetodoSiNoExiste("Pago contraentrega");
        crearMetodoSiNoExiste("Transferencia Bancaria");

        List<String> productosCatalogo = List.of(
                "Cebolla", "Zanahoria", "Tomate", "Uvas silvestres", "Ajo", "Lechuga",
                "Manzanas", "Bananos", "Zanahorias", "Cebolla morada", "Papa", "Uva morada silvestre"
        );
        productosCatalogo.forEach(this::crearProductoSiNoExiste);
        productoRepository.findByActivoTrueOrderByNombreAsc().forEach(this::crearStockSiNoExiste);
    }

    private void crearMetodoSiNoExiste(String nombre) {
        if (metodoPagoRepository.findByNombreIgnoreCaseAndActivoTrue(nombre).isEmpty()) {
            MetodoPago metodo = new MetodoPago();
            metodo.setNombre(nombre);
            metodo.setActivo(true);
            metodoPagoRepository.save(metodo);
        }
    }

    private void crearProductoSiNoExiste(String nombre) {
        if (productoRepository.findFirstByNombreIgnoreCaseAndActivoTrue(nombre).isEmpty()) {
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion("Producto disponible en el catálogo público de Agroconecta");
            producto.setUnidadMedida("unidad");
            producto.setPrecioUnitario(new BigDecimal("5000.00"));
            producto.setActivo(true);
            productoRepository.save(producto);
        }
    }

    private void crearStockSiNoExiste(Producto producto) {
        if (!stockRepository.existsByProductoId(producto.getId())) {
            Stock stock = new Stock();
            stock.setUsuarioId(2L);
            stock.setProducto(producto);
            stock.setCantidad(new BigDecimal("20.000"));
            stockRepository.save(stock);
        }
    }
}
