package com.fieldright.fr.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class PrecoDTO {

    private BigDecimal preco;
    private BigDecimal novoPreco;
    private String unidadeOrigem;
    private String unidadeDestino;

}
