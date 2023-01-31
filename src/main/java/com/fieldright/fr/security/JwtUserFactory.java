package com.fieldright.fr.security;

import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.JwtUser;
import com.fieldright.fr.entity.security.UserAuthenticated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtUserFactory {

    public static JwtUser create(UserDTO user){
        return new JwtUser(user.getId(),user.getEmail(),user.getPassword(),user.isActive(),user.isAlterPassword());
    }

    public static UserAuthenticated getAuthenticated(UserDTO user){
        return new UserAuthenticated(user.getId(),user.getEmail());
    }
}