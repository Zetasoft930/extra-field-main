package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.util.enums.TipoVeiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class UserCompraDTO{


    private long id;
    private String nif;
    private String cnh;
    private String cpf;
    private String cnpj;
    private String email;
    private String phone;
    private String avatar;
    private String perfil;
    private String country;
    private String renavam;
    private boolean active;
    private String lastName;
    private String firstName;
    private Endereco endereco;
    private Timestamp createdAt;
    private boolean alterPassword;
    private Date proximaDesativacao;
    private String bilheteIdentidade;
    private Boolean possuiEntregadores;
    private BigDecimal purchaseTotal = BigDecimal.ZERO;
}
