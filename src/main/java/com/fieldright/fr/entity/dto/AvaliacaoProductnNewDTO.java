package com.fieldright.fr.entity.dto;

import lombok.Data;

@Data
public class AvaliacaoProductnNewDTO {

    private byte estrelas;
    private String comentario;
    private long avaliadorId;
    private long productId;
}
