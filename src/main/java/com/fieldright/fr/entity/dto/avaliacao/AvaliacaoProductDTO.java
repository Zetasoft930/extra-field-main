package com.fieldright.fr.entity.dto.avaliacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoProductDTO {

    private Long productId;
    private String productName;
    private BigDecimal estrela;

    private Set<AvaliadorDTO> avaliadores = new HashSet<>();
}
