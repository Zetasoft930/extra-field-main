package com.fieldright.fr.entity.enums;

public enum Status {

    NOVA("NOVA"),
    AGUARDANDO_ENTREGADOR("AGUARDANDO_ENTREGADOR"),
    CANCELADA("CANCELADA"),
    FINALIZADA("FINALIZADA");


    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
