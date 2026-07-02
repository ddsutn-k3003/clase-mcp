package ar.edu.utn.ddsi.mcpbilletera.controller;

import ar.edu.utn.ddsi.mcpbilletera.dto.TransferenciaConfirmadaResponse;
import ar.edu.utn.ddsi.mcpbilletera.dto.TransferenciaIniciadaResponse;
import ar.edu.utn.ddsi.mcpbilletera.service.CuentaService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final CuentaService cuentaService;

    public TransferenciaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    public record IniciarTransferenciaRequest(String aliasOrigen, String aliasDestino,
                                              BigDecimal monto, String descripcion) {}

    @PostMapping
    public TransferenciaIniciadaResponse iniciar(@RequestBody IniciarTransferenciaRequest request) {
        return cuentaService.iniciarTransferencia(
                request.aliasOrigen(), request.aliasDestino(), request.monto(), request.descripcion());
    }

    @PostMapping("/{id}/confirmar")
    public TransferenciaConfirmadaResponse confirmar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return cuentaService.confirmarTransferencia(id, body.get("codigo"));
    }

    // Este es el endpoint que van a llamar por Postman en clase, simulando el "SMS".
    // A propósito NO tiene @McpTool ni @McpResource — no se agrega el sábado.
    @GetMapping("/{id}/codigo")
    public Map<String, String> obtenerCodigo(@PathVariable Long id) {
        return Map.of("codigo", cuentaService.obtenerCodigoConfirmacion(id));
    }
}