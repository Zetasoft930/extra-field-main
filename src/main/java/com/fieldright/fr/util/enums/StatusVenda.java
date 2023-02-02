package com.fieldright.fr.util.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusVenda {
    NOVA("NOVA"),//Nova venda gerada
    EM_PREPARACAO("EM_PREPARACAO"),//o vendedor aceitou de vender o produto e nesse momento começou a preparação deste
    AGUARDANDO_ENTREGADOR("AGUARDANDO_ENTREGADOR"),//terminou a preparação do produto porém ainda não está à caminho.
    A_CAMINHO("A_CAMINHO"),//O entregador já levou o produto pra entrega
    FINALIZADA("FINALIZADA"),//o produto já foi entregue
    CANCELADA("REJEITADA");//o vendedor rejeitou a venda. a compra não será realizada

    private String text;

    public String getText() {
        return text;
    }
}
