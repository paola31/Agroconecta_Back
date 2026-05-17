package com.agroconecta.stock;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import com.agroconecta.stock.dto.StockResponse;
import com.agroconecta.stock.dto.StockRequest;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockResponse> listar() {
        return stockService.listar()
                .stream()
                .map(StockResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public StockResponse buscarPorId(@PathVariable Long id) {
        Stock stock = stockService.buscarPorId(id);
        return StockResponse.fromEntity(stock);
    }

    @PostMapping
    public ResponseEntity<StockResponse> crear(@Valid @RequestBody StockRequest request) {
        Stock stockCreado = stockService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(StockResponse.fromEntity(stockCreado));
    }

    @PutMapping("/{id}")
    public StockResponse actualizar(@PathVariable Long id, @Valid @RequestBody StockRequest request) {
        Stock stockActualizado = stockService.actualizar(id, request);
        return StockResponse.fromEntity(stockActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        stockService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
