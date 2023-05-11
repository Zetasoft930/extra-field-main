package com.fieldright.fr.util.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusVenda {
    NOVA("NOVA",1L),//Nova venda gerada
    EM_PREPARACAO("EM_PREPARACAO",2L),//o vendedor aceitou de vender o produto e nesse momento começou a preparação deste
    AGUARDANDO_ENTREGADOR("AGUARDANDO_ENTREGADOR",3L),//terminou a preparação do produto porém ainda não está à caminho.
    A_CAMINHO("A_CAMINHO",4L),//O entregador já levou o produto pra entrega
    FINALIZADA("FINALIZADA",5L),//o produto já foi entregue
    CANCELADA("REJEITADA",6L);//o vendedor rejeitou a venda. a compra não será realizada

    private String text;
    private Long code;

    public String getText() {
        return text;
    }


    public static StatusVenda toEnum(Long code)throws Exception{

        for(StatusVenda statusVenda : StatusVenda.values() ){

            if(statusVenda.code == code)
                return statusVenda;
        }

        throw new RuntimeException("Status invalido.Lista dos Status valido:" +
                "       NOVA(Codigo = 1)\n" +
                "    EM_PREPARACAO(Codigo = 2)\n" +
                "    AGUARDANDO_ENTREGADOR(Codigo = 3)\n" +
                "    A_CAMINHO(Codigo = 4)\n" +
                "    FINALIZADA(Codigo = 5)\n" +
                "    CANCELADA(Codigo = 6)");

    }
}
