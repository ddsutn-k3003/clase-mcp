package ar.edu.utn.ddsi.mcpbilletera.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String alias, BigDecimal saldoActual, BigDecimal montoSolicitado) {
        super("Saldo insuficiente en la cuenta " + alias + ". Saldo actual: " + saldoActual
                + ", monto solicitado: " + montoSolicitado);
    }
}