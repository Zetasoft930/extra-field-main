package com.fieldright.fr.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ExpoPushDTO {

    private String to;
    @NotBlank(message = "title e Obrigatorio")
    @NotNull(message = "title e Obrigatorio")
    private String title;
    @NotBlank(message = "body e Obrigatorio")
    @NotNull(message = "body e Obrigatorio")
    private String  body;
}
