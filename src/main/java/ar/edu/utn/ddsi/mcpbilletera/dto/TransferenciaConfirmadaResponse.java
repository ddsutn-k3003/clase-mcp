package ar.edu.utn.ddsi.mcpbilletera.dto;

import java.math.BigDecimal;

public record TransferenciaConfirmadaResponse(
        Long movimientoId,
        BigDecimal monto,
        String mensaje
) {
}