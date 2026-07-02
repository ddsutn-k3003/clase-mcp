package ar.edu.utn.ddsi.mcpbilletera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuentaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleCuentaNoEncontrada(CuentaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(TransferenciaNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> handleTransferenciaNoEncontrada(TransferenciaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({SaldoInsuficienteException.class, MontoInvalidoException.class, CodigoInvalidoException.class})
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler({TransferenciaExpiradaException.class, TransferenciaYaProcesadaException.class})
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}