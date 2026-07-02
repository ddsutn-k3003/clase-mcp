package ar.edu.utn.ddsi.mcpbilletera.controller;

import ar.edu.utn.ddsi.mcpbilletera.dto.MovimientoResponse;
import ar.edu.utn.ddsi.mcpbilletera.dto.SaldoResponse;
import ar.edu.utn.ddsi.mcpbilletera.service.CuentaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/{alias}/saldo")
    public SaldoResponse saldo(@PathVariable String alias) {
        return cuentaService.consultarSaldo(alias);
    }

    @GetMapping("/{alias}/movimientos")
    public List<MovimientoResponse> movimientos(@PathVariable String alias,
                                                @RequestParam(defaultValue = "10") int limite) {
        return cuentaService.listarMovimientos(alias, limite);
    }
}