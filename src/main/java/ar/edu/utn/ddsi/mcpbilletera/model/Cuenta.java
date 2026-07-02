package ar.edu.utn.ddsi.mcpbilletera.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal saldo;

}
