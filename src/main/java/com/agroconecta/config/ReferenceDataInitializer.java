package com.agroconecta.config;

import com.agroconecta.pedido.MetodoPago;
import com.agroconecta.pedido.MetodoPagoRepository;
import com.agroconecta.producto.Producto;
import com.agroconecta.producto.ProductoRepository;
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

    public ReferenceDataInitializer(MetodoPagoRepository metodoPagoRepository, ProductoRepository productoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.productoRepository = productoRepository;
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
}
