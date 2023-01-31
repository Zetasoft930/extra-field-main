package com.fieldright.fr.entity.architecture;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class MoneyConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String moeda;
    private String moedaAbreviacao;
    private String pais;
    @Size(min = 3, max = 3)
    private String paisCodigo;
    private double valorMínimoCompra;
}
