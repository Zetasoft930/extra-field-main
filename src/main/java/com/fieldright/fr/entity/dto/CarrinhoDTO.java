package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarrinhoDTO {

    private long id;
    private long compradorId;
    private List<CompraDTO> compras;
    private String formaPagamento;
    private BigDecimal vlTotal;
    private String statusCompra;
    private BigDecimal taxaEntrega;
    private String codigoPagamento;
}
