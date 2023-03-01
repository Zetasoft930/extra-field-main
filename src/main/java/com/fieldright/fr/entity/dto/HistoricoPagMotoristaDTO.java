package com.fieldright.fr.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class HistoricoPagMotoristaDTO {

    @NotNull(message = "userId obrigatorio")
    private Long userId;
    @NotNull(message = "dataInicio obrigatorio")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp dataInicio;
    @NotNull(message = "dataTermino obrigatorio")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp dataTermino;
}
