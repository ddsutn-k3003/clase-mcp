package ar.edu.utn.ddsi.mcpbilletera.repository;

import ar.edu.utn.ddsi.mcpbilletera.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByAlias(String alias);
}