package com.fieldright.fr.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CidadeAtuacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String pais;
    private int codigoUF;
    private String siglaUF;
    private String nomeUF;
    private int codigoMunicipio;
    private String nomeMunicipio;

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    public int getCodigoUF() {
        return codigoUF;
    }

    public String getPais() {
        return pais;
    }

    public String getSiglaUF() {
        return siglaUF;
    }

    public String getNomeUF() {
        return nomeUF;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    public Long getId() {
        return id;
    }
}
