package ar.edu.utn.ddsi.mcpbilletera.service;

import ar.edu.utn.ddsi.mcpbilletera.dto.*;
import ar.edu.utn.ddsi.mcpbilletera.exception.*;
import ar.edu.utn.ddsi.mcpbilletera.model.Cuenta;
import ar.edu.utn.ddsi.mcpbilletera.model.Movimiento;
import ar.edu.utn.ddsi.mcpbilletera.model.TransferenciaPendiente;
import ar.edu.utn.ddsi.mcpbilletera.model.enums.EstadoTx;
import ar.edu.utn.ddsi.mcpbilletera.model.enums.TipoMovimiento;
import ar.edu.utn.ddsi.mcpbilletera.repository.CuentaRepository;
import ar.edu.utn.ddsi.mcpbilletera.repository.MovimientoRepository;
import ar.edu.utn.ddsi.mcpbilletera.repository.TransferenciaPendienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CuentaService {

    private static final int MINUTOS_EXPIRACION = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final TransferenciaPendienteRepository transferenciaRepository;

    public CuentaService(CuentaRepository cuentaRepository,
                         MovimientoRepository movimientoRepository,
                         TransferenciaPendienteRepository transferenciaRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.transferenciaRepository = transferenciaRepository;
    }

    // ---------- Consultas ----------

    public SaldoResponse consultarSaldo(String alias) {
        Cuenta cuenta = buscarCuentaPorAlias(alias);
        return new SaldoResponse(cuenta.getAlias(), cuenta.getTitular(), cuenta.getSaldo());
    }

    public List<MovimientoResponse> listarMovimientos(String alias, int limite) {
        Cuenta cuenta = buscarCuentaPorAlias(alias);
        return movimientoRepository
                .findByCuentaOrigenIdOrCuentaDestinoIdOrderByFechaDesc(cuenta.getId(), cuenta.getId())
                .stream()
                .limit(limite)
                .map(m -> new MovimientoResponse(m.getId(), m.getTipo().name(), m.getMonto(), m.getFecha(), m.getDescripcion()))
                .toList();
    }

    // ---------- Transferencia: paso 1, iniciar ----------

    @Transactional
    public TransferenciaIniciadaResponse iniciarTransferencia(String aliasOrigen, String aliasDestino,
                                                              BigDecimal monto, String descripcion) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInvalidoException("El monto tiene que ser mayor a cero.");
        }
        if (aliasOrigen.equalsIgnoreCase(aliasDestino)) {
            throw new MontoInvalidoException("No podés transferirte a tu propia cuenta.");
        }

        Cuenta origen = buscarCuentaPorAlias(aliasOrigen);
        Cuenta destino = buscarCuentaPorAlias(aliasDestino);

        if (origen.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException(aliasOrigen, origen.getSaldo(), monto);
        }

        String codigo = generarCodigo();
        LocalDateTime ahora = LocalDateTime.now();

        TransferenciaPendiente transferencia = new TransferenciaPendiente();
        transferencia.setCuentaOrigenId(origen.getId());
        transferencia.setCuentaDestinoId(destino.getId());
        transferencia.setMonto(monto);
        transferencia.setDescripcion(descripcion);
        transferencia.setCodigoConfirmacion(codigo);
        transferencia.setEstado(EstadoTx.PENDIENTE);
        transferencia.setCreadoEn(ahora);
        transferencia.setExpiraEn(ahora.plusMinutes(MINUTOS_EXPIRACION));

        transferencia = transferenciaRepository.save(transferencia);

        return new TransferenciaIniciadaResponse(
                transferencia.getId(),
                "Transferencia #" + transferencia.getId() + " iniciada. Pedile al usuario el código de "
                        + "verificación para confirmarla (válido por " + MINUTOS_EXPIRACION + " minutos)."
        );
    }

    // ---------- Transferencia: paso 2, confirmar ----------

    @Transactional
    public TransferenciaConfirmadaResponse confirmarTransferencia(Long idTransferencia, String codigo) {
        TransferenciaPendiente transferencia = transferenciaRepository.findById(idTransferencia)
                .orElseThrow(() -> new TransferenciaNoEncontradaException(idTransferencia));

        if (transferencia.getEstado() == EstadoTx.CONFIRMADA) {
            throw new TransferenciaYaProcesadaException(idTransferencia);
        }
        if (transferencia.getEstado() == EstadoTx.EXPIRADA
                || LocalDateTime.now().isAfter(transferencia.getExpiraEn())) {
            transferencia.setEstado(EstadoTx.EXPIRADA);
            transferenciaRepository.save(transferencia);
            throw new TransferenciaExpiradaException(idTransferencia);
        }
        if (!transferencia.getCodigoConfirmacion().equals(codigo)) {
            throw new CodigoInvalidoException();
        }

        Cuenta origen = cuentaRepository.findById(transferencia.getCuentaOrigenId())
                .orElseThrow(() -> new IllegalStateException("Cuenta origen inexistente, dato corrupto"));
        Cuenta destino = cuentaRepository.findById(transferencia.getCuentaDestinoId())
                .orElseThrow(() -> new IllegalStateException("Cuenta destino inexistente, dato corrupto"));

        // Re-validamos saldo por si cambió entre iniciar y confirmar
        if (origen.getSaldo().compareTo(transferencia.getMonto()) < 0) {
            throw new SaldoInsuficienteException(origen.getAlias(), origen.getSaldo(), transferencia.getMonto());
        }

        origen.setSaldo(origen.getSaldo().subtract(transferencia.getMonto()));
        destino.setSaldo(destino.getSaldo().add(transferencia.getMonto()));
        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        Movimiento movimiento = new Movimiento();
        movimiento.setCuentaOrigenId(origen.getId());
        movimiento.setCuentaDestinoId(destino.getId());
        movimiento.setMonto(transferencia.getMonto());
        movimiento.setTipo(TipoMovimiento.TRANSFERENCIA);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setDescripcion(transferencia.getDescripcion());
        movimiento = movimientoRepository.save(movimiento);

        transferencia.setEstado(EstadoTx.CONFIRMADA);
        transferenciaRepository.save(transferencia);

        return new TransferenciaConfirmadaResponse(
                movimiento.getId(),
                movimiento.getMonto(),
                "Transferencia confirmada. Se transfirieron " + movimiento.getMonto()
                        + " de " + origen.getAlias() + " a " + destino.getAlias() + "."
        );
    }

    // ---------- Solo para el endpoint "Postman" (el canal fuera de la IA) ----------

    public String obtenerCodigoConfirmacion(Long idTransferencia) {
        TransferenciaPendiente transferencia = transferenciaRepository.findById(idTransferencia)
                .orElseThrow(() -> new TransferenciaNoEncontradaException(idTransferencia));

        if (transferencia.getEstado() != EstadoTx.PENDIENTE
                || LocalDateTime.now().isAfter(transferencia.getExpiraEn())) {
            throw new TransferenciaExpiradaException(idTransferencia);
        }
        return transferencia.getCodigoConfirmacion();
    }

    // ---------- Privados ----------

    private Cuenta buscarCuentaPorAlias(String alias) {
        return cuentaRepository.findByAlias(alias)
                .orElseThrow(() -> new CuentaNoEncontradaException(alias));
    }

    private String generarCodigo() {
        int codigo = RANDOM.nextInt(1_000_000); // 0 a 999999
        return String.format("%06d", codigo);   // siempre 6 dígitos, con ceros a la izquierda
    }
}