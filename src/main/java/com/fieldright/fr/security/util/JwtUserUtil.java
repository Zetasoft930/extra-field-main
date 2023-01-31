package com.fieldright.fr.security.util;

import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.security.JwtUserFactory;
import com.fieldright.fr.service.interfaces.UserService;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@Component
@CrossOrigin
public class JwtUserUtil {

    private static UserService userService;

    public JwtUserUtil(UserService userService){
        JwtUserUtil.userService = userService;
    }

    public static UserAuthenticated getUserAuthenticated(){
        UserDTO userByEmail = userService.internalFindUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!userByEmail.isActive()) {
            throw new DisabledException("User is disabled");
        }

        return JwtUserFactory.getAuthenticated(userByEmail);
    }
}
