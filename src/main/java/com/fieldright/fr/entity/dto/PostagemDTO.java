package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
public class PostagemDTO {

    private long id;
    @NotBlank(message = "Titulo Obrigatorio")
    private String titulo;
    @NotBlank(message = "descricao Obrigatorio")
    private String descricao;
    private String imagem;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime data;
    @NotNull(message = "categoria Obrigatorio")
    private Long categoria;


}
