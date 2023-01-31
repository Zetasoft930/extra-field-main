package com.fieldright.fr.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCompra {
    AGUARDANDO_PAGAMENTO("AGUARDANDO_PAGAMENTO"),//a compra já existe porém o pagamento ainda não foi confirmada
    AGUARDANDO_CONFIRMACAO("AGUARDANDO_CONFIRMAÇÃO"),//aguardando confirmação do vendedor (que pode aceitar ou recusar)
    EM_PREPARACAO("EM_PREPARACAO"),//O vendedor confirmou e está preparando a entrega ou já foi preparado porém ainda não está à caminho
    A_CAMINHO("A_CAMINHO"),//O entregador já est´[a a caminho
    ENTREGUE("ENTREGUE"),//compra finalizada
    CANCELADA("CANCELADA");

    private String text;
}
