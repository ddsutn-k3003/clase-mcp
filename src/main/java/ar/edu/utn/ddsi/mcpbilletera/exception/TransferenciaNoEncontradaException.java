package ar.edu.utn.ddsi.mcpbilletera.exception;

public class TransferenciaNoEncontradaException extends RuntimeException {
    public TransferenciaNoEncontradaException(Long id) {
        super("No existe una transferencia pendiente con id: " + id);
    }
}