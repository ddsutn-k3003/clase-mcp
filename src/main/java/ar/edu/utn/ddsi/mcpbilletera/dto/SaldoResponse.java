package ar.edu.utn.ddsi.mcpbilletera.dto;

import java.math.BigDecimal;

public record SaldoResponse(String alias, String titular, BigDecimal saldo) {
}