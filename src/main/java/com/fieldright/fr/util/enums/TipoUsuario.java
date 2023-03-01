package com.fieldright.fr.util.enums;

public enum TipoUsuario {

    MOTORISTA("Motorista"),
    VENDEDOR("Vendedor"),
    USUARIO("Usuario"),
    COMPRADOR("Comprador");

    private String text;

    TipoUsuario(String text)
    {
        this.text = text;

    }

    public String getText() {
        return text;
    }
}
