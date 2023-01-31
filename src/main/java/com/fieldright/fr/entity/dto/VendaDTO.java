package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendaDTO {

    private long id;
    private String status;
    private long vendedorId;
    private long compradorId;
    private BigDecimal vlTotal;
    private BigDecimal taxaFieldrightVenda;
    private BigDecimal taxaFieldrightEntrega;
    private BigDecimal valorLiquido;
    private Timestamp createdAt;
    private String vendedorName;
    private String vendedorPhone;
    private String compradorName;
    private Timestamp aCaminhoAt;
    private String compradorPhone;
    private Timestamp rejeitadaAt;
    private String formaPagamento;
    private Timestamp confirmadaAt;
    private Timestamp finalizadaAt;
    private String enderecoEntrega;
    private List<CompraDTO> compras;
}
