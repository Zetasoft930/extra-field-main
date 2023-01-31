package com.fieldright.fr.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fieldright.fr.entity.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {

    private String token;
    private UserDTO user;
    private List<String> categories;
}
