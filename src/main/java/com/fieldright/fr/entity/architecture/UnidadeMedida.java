package com.fieldright.fr.entity.architecture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String simbolo;
    private String texto;

    public UnidadeMedida(long id) {
        this.id = id;
    }

    @Override public String toString() {
        return new StringBuilder()
                        .append(simbolo)
                        .append(" - ")
                        .append("(")
                        .append(texto)
                        .append(")")
                        .toString();
    }
}
