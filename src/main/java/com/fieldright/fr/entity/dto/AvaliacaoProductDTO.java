package com.fieldright.fr.entity.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvaliacaoProductDTO {

		private long id;
	    private byte estrelas;
	    private String comentario;
	    private long avaliadorId;
	    private long productId;
	    private Timestamp createdAt;
}
