package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class PostagemFilterDTO {

    @NotNull(message = "Status e obrigatorio")
    private Integer status;
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "dataInicio e obrigatorio")
    private String dataInicio;
    @NotNull(message = "dataTermino e obrigatorio")
   // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dataTermino;
}
