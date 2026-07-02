package ar.edu.utn.ddsi.mcpbilletera.repository;

import ar.edu.utn.ddsi.mcpbilletera.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaOrigenIdOrCuentaDestinoIdOrderByFechaDesc(
            Long cuentaOrigenId, Long cuentaDestinoId);
}
