package ar.edu.utn.ddsi.mcpbilletera.exception;

public class MontoInvalidoException extends RuntimeException {
    public MontoInvalidoException(String mensaje) {
        super(mensaje);
    }
}