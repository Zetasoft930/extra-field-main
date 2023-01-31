package com.fieldright.fr.entity.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Esta classe serve apenas para recuperar as informações necessárias do usuário que está logado na api
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticated {

    private long id;
    private String email;
}
