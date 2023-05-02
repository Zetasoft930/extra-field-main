package com.fieldright.fr.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoVendidoDTO {

    private Long id;
    private String name;
    private BigDecimal qtd;
    private List<String> image = new ArrayList<>();
}
