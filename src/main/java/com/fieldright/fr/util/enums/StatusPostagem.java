package com.fieldright.fr.util.enums;

public enum StatusPostagem {

    ACTIVADO("ACTIVADO",1),
    PENDENTE("PENDENTE",2),
    DESACTIVADO("DESACTIVADO",3),
    ELIMINADO("ELIMINADO",4);

    private String text;
    private Integer code;

    StatusPostagem(String text,Integer code)
    {
            this.text = text;
            this.code = code;
    }

    public static StatusPostagem toEnum(Integer code)
    {
        for(StatusPostagem s : StatusPostagem.values())
        {
            if(s.code == code)
            {
                return s;
            }
        }
        throw new RuntimeException("Status nao foi encontrado");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return  text ;
    }
}
