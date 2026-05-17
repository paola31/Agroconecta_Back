package com.agroconecta.stock;

import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import com.agroconecta.producto.ProductoService;
import com.agroconecta.stock.dto.StockRequest;
import org.springframework.stereotype.Service;
import com.agroconecta.producto.Producto;
import java.math.BigDecimal;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductoService productoService;

    public StockService(StockRepository stockRepository, ProductoService productoService) {
        this.stockRepository = stockRepository;
        this.productoService = productoService;
    }

    @Transactional(readOnly = true)
    public List<Stock> listar() {
        return stockRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Stock buscarPorId(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de stock no encontrado con id " + id));
    }

    @Transactional
    public Stock crear(StockRequest request) {
        validarStock(request);
        validarRegistroUnico(request.getUsuarioId(), request.getProductoId());

        Producto producto = productoService.buscarPorId(request.getProductoId());

        Stock stock = new Stock();
        stock.setUsuarioId(request.getUsuarioId());
        stock.setProducto(producto);
        stock.setCantidad(request.getCantidad());

        return stockRepository.save(stock);
    }

    @Transactional
    public Stock actualizar(Long id, StockRequest request) {
        validarStock(request);

        Stock stock = buscarPorId(id);
        Producto producto = productoService.buscarPorId(request.getProductoId());

        stockRepository.findByUsuarioIdAndProductoId(request.getUsuarioId(), request.getProductoId())
                .filter(registro -> !registro.getId().equals(id))
                .ifPresent(registro -> {
                    throw new IllegalArgumentException("Ya existe stock para el usuario y producto seleccionados");
                });

        stock.setUsuarioId(request.getUsuarioId());
        stock.setProducto(producto);
        stock.setCantidad(request.getCantidad());

        return stockRepository.save(stock);
    }

    @Transactional
    public void eliminar(Long id) {
        Stock stock = buscarPorId(id);
        stockRepository.delete(stock);
    }

    private void validarStock(StockRequest request) {
        if (request.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (request.getProductoId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        BigDecimal cantidad = request.getCantidad();
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a cero");
        }
    }

    private void validarRegistroUnico(Long usuarioId, Long productoId) {
        stockRepository.findByUsuarioIdAndProductoId(usuarioId, productoId)
                .ifPresent(registro -> {
                    throw new IllegalArgumentException("Ya existe stock para el usuario y producto seleccionados");
                });
    }
}
