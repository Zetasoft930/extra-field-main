package com.fieldright.fr.util.enums;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoVeiculo {
    BIKE("BIKE"),
    MOTO("MOTO"),
    CARRO("CARRO"),
    EMPRESA("EMPRESA");

    private String text;
}
