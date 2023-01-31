package com.fieldright.fr.util.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusEntrega {
    NOVA("NOVA"), //Nova entrega disponível
    TOMADA("TOMADA"),//Algum entregador já aceitou essa entrega
    A_CAMINHO("A_CAMINHO"),//O entregador já está a caminho com o produto comprado
    FINALIZADA("FINALIZADA");//O produto já foi entregue

    private String text;
}
