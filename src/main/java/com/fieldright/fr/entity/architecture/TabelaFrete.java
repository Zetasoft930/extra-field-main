package com.fieldright.fr.entity.architecture;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class TabelaFrete {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private Double minimo;
    private Double maximo;
    private BigDecimal valor;

    public boolean correspondeCom(double pesoCubado) {
        return pesoCubado >= minimo && pesoCubado <= maximo;
    }
}
