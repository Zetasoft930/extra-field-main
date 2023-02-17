package com.fieldright.fr.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class UnidadeMedidaConverterDTO {


    private Long id;
    @NotNull(message = "unidadeMedidaOrigemId e obrigatorio")
    private Long unidadeMedidaOrigemId;
    @NotNull(message = "unidadeMedidaDestinoId e obrigatorio")
    private Long unidadeMedidaDestinoId;
    @NotNull(message = "equivale e obrigatorio")
    @Min(value = 0,message = "Valor minimo da equivale Zero")
    private BigDecimal equivale;

}
