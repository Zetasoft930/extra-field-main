package com.fieldright.fr.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ProductPriceDTO {

    @NotNull(message = "productId e obrigatorio")
    private Long productId;
    @NotNull(message = "unidadeMedida e obrigatorio")
    @NotBlank(message = "unidadeMedida e obrigatorio")
    private String unidadeMedida;
    @NotNull(message = "qtd e obrigatorio")
    private BigDecimal qtd;
}
