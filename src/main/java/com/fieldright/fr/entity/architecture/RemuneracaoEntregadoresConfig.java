package com.fieldright.fr.entity.architecture;

import com.fieldright.fr.util.enums.TipoVeiculo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class RemuneracaoEntregadoresConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private BigDecimal valorBase;
    private double distanciaMaxima;
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipoVeiculo;
    private BigDecimal valorAdicionalPorKm;
    private boolean ultraPassaDistanciaMaxima;
}
