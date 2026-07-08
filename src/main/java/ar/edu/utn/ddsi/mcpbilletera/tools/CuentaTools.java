package ar.edu.utn.ddsi.mcpbilletera.tools;

import ar.edu.utn.ddsi.mcpbilletera.dto.MovimientoResponse;
import ar.edu.utn.ddsi.mcpbilletera.dto.SaldoResponse;
import ar.edu.utn.ddsi.mcpbilletera.dto.TransferenciaConfirmadaResponse;
import ar.edu.utn.ddsi.mcpbilletera.dto.TransferenciaIniciadaResponse;
import ar.edu.utn.ddsi.mcpbilletera.service.CuentaService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CuentaTools {

    private CuentaService cuentaService;

    public CuentaTools(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @McpTool(name = "consultarSaldo", description = "Consulta el saldo de una cuenta")
    public SaldoResponse consultarSaldo(@McpToolParam(description = "Alias de la cuenta", required = true)
            String alias) {
        return cuentaService.consultarSaldo(alias);
    }

    @McpTool(name = "listar_movimientos",
            description = "Lista los últimos movimientos (depósitos y transferencias) de una cuenta")
    public List<MovimientoResponse> listarMovimientos(
            @McpToolParam(description = "Alias de la cuenta, ej: juan.perez", required = true)
            String alias,
            @McpToolParam(description = "Cantidad máxima de movimientos a devolver. Si no se especifica, se usan 10.", required = false)
            Integer limite) {
        return cuentaService.listarMovimientos(alias, limite != null ? limite : 10);
    }

    @McpTool(name = "iniciar_transferencia",
            description = "Inicia una transferencia de dinero entre dos cuentas. No mueve el dinero todavía: "
                    + "genera un código de verificación de 6 dígitos que el usuario debe obtener por otro canal "
                    + "y comunicar para confirmar la operación con la tool confirmar_transferencia.")
    public TransferenciaIniciadaResponse iniciarTransferencia(
            @McpToolParam(description = "Alias de la cuenta de origen", required = true)
            String aliasOrigen,
            @McpToolParam(description = "Alias de la cuenta de destino", required = true)
            String aliasDestino,
            @McpToolParam(description = "Monto a transferir, mayor a cero", required = true)
            BigDecimal monto,
            @McpToolParam(description = "Descripción opcional de la transferencia", required = false)
            String descripcion) {
        return cuentaService.iniciarTransferencia(aliasOrigen, aliasDestino, monto, descripcion);
    }

    @McpTool(name = "confirmar_transferencia",
            description = "Confirma y ejecuta una transferencia previamente iniciada, validando el código "
                    + "de verificación de 6 dígitos que el usuario obtuvo por otro canal (no por este chat).")
    public TransferenciaConfirmadaResponse confirmarTransferencia(
            @McpToolParam(description = "Id de la transferencia devuelto por iniciar_transferencia", required = true)
            Long idTransferencia,
            @McpToolParam(description = "Código de verificación de 6 dígitos", required = true)
            String codigo) {
        return cuentaService.confirmarTransferencia(idTransferencia, codigo);
    }
}
