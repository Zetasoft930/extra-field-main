package com.fieldright.fr.entity.dto.avaliacao;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AvaliadorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String avatar;
    private BigDecimal estrela;
}
