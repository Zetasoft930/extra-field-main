package com.fieldright.fr.util.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FormaPagamento {
    CARTAO_DEBITO("C. Debito"),
    CARTAO_CREDITO("C. Credito");

    private String text;
}
