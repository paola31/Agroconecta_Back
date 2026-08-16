package com.agroconecta.pedido;

import com.agroconecta.pedido.dto.PedidoRequest;
import com.agroconecta.pedido.dto.PedidoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@Valid @RequestBody PedidoRequest request, Authentication authentication) {
        Long clienteId = (Long) authentication.getDetails();
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crear(request, clienteId));
    }
}
