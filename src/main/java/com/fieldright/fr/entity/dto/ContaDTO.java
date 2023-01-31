package com.fieldright.fr.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
public class ContaDTO {

    private BigDecimal saldo;
    private String emailPagSeguro;
    private String chavePix;
    private String nome;
    private String instituicao;
    private Character tipoConta;
    private String agencia;
    private String nuConta;
    private Character digito;
    private String CPFouCNPJ;
    private Timestamp modifiedAt;
}
