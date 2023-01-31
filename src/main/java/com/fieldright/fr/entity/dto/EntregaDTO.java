package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.util.enums.StatusEntrega;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntregaDTO {

    private long id;
    private long vendaId;
    private long entregadorId;
    private double pesoCubado;
    private Timestamp aceitaAt;
    private Timestamp createdAt;
    private String vendedorName;
    private String compradorName;
    private String vendedorPhone;
    private Timestamp entregueAt;
    private Timestamp aCaminhoAt;
    private Endereco enderecoLoja;
    private String entregadorName;
    private String compradorPhone;
    private String entregadorPhone;
    private String distanciaEntrega;
    private Endereco enderecoEntrega;
    private BigDecimal valorRemuneracao;
    private StatusEntrega statusEntrega;
    private Map<String, BigDecimal> valoresRemuneracao;
}
