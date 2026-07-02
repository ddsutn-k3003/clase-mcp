package ar.edu.utn.ddsi.mcpbilletera.exception;

public class CodigoInvalidoException extends RuntimeException {
    public CodigoInvalidoException() {
        super("El código de confirmación es incorrecto.");
    }
}