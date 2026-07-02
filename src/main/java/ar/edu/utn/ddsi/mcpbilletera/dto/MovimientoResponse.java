package ar.edu.utn.ddsi.mcpbilletera.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        String tipo,
        BigDecimal monto,
        LocalDateTime fecha,
        String descripcion
) {
}