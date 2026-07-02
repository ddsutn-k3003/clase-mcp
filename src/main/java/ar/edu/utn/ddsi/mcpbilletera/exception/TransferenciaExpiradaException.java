package ar.edu.utn.ddsi.mcpbilletera.exception;

public class TransferenciaExpiradaException extends RuntimeException {
    public TransferenciaExpiradaException(Long id) {
        super("La transferencia #" + id + " expiró. Iniciá una nueva.");
    }
}