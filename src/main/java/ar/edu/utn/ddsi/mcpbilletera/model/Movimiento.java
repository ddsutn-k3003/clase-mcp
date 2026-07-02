package ar.edu.utn.ddsi.mcpbilletera.model;

import ar.edu.utn.ddsi.mcpbilletera.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuenta_origen_id") //null si es un deposito
    private Long cuentaOrigenId;

    @Column(name = "cuenta_destino_id", nullable = false)
    private Long cuentaDestinoId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    private String descripcion;

}
