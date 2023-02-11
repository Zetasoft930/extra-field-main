package com.fieldright.fr.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UnidadeMedidaConverterDTO {


    private Long id;
    private Long unidadeMedidaOrigemId;
    private Long unidadeMedidaDestinoId;
    private BigDecimal equivale;

}
