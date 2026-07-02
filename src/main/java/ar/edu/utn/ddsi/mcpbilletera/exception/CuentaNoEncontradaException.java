package ar.edu.utn.ddsi.mcpbilletera.exception;

public class CuentaNoEncontradaException extends RuntimeException {
    public CuentaNoEncontradaException(String alias) {
        super("No existe una cuenta con alias: " + alias);
    }
}