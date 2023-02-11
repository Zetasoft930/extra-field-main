package com.fieldright.fr.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductPriceDTO {

    @NotNull(message = "productId e obrigatorio")
    private Long productId;
    @NotNull(message = "unidadeMedida e obrigatorio")
    @NotBlank(message = "unidadeMedida e obrigatorio")
    private String unidadeMedida;
}
