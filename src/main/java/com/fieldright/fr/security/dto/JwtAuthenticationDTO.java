package com.fieldright.fr.security.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JwtAuthenticationDTO {

    @NotBlank(message = "The password must not be blank")
    private String password;
    @Email (message = "This email is not valid")
    @NotBlank(message = "The email must not be blank")
    private String email;
}
