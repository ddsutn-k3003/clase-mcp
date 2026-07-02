package ar.edu.utn.ddsi.mcpbilletera.model;

import ar.edu.utn.ddsi.mcpbilletera.model.enums.EstadoTx;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias_pendientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuenta_origen_id", nullable = false)
    private Long cuentaOrigenId;

    @Column(name = "cuenta_destino_id", nullable = false)
    private Long cuentaDestinoId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    private String descripcion;

    @Column(name = "codigo_confirmacion", nullable = false, length = 6)
    private String codigoConfirmacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTx estado;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "expira_en", nullable = false)
    private LocalDateTime expiraEn;

}
