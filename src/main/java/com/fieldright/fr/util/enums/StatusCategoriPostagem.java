package com.fieldright.fr.util.enums;

public enum StatusCategoriPostagem {

    ACTIVADO(1,"ACTIVADO"),
    ELIMINADO(2,"ELIMINADO");

    private Integer code;
    private String text;

    StatusCategoriPostagem(Integer code,String text) {
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static StatusCategoriPostagem toEnum(Integer code) {

        for(StatusCategoriPostagem s : StatusCategoriPostagem.values())
        {
            if(s.code == code)
                return s;
        }

        throw new RuntimeException("Status invalid");
    }
}
