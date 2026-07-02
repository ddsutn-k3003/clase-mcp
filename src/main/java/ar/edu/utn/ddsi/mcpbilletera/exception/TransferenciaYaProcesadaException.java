package ar.edu.utn.ddsi.mcpbilletera.exception;

public class TransferenciaYaProcesadaException extends RuntimeException {
    public TransferenciaYaProcesadaException(Long id) {
        super("La transferencia #" + id + " ya fue confirmada anteriormente.");
    }
}