package com.fieldright.fr.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String USER_DISABLED_MESSAGE="User disabled";
    private static final String BAD_CREDENTIALS_MESSAGE="Bad credentials";

    /**
     * Este método é apenas para personalizar a mensagem que deveria aparecer para o usuário quando ele não tiver acesso à algum endpoint
     * @param request
     * @param response
     * @param e
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if(e.getClass()== DisabledException.class){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, USER_DISABLED_MESSAGE);
        }else
            if(e.getClass()== BadCredentialsException.class) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, BAD_CREDENTIALS_MESSAGE);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied. You must be authenticated in the " +
                    "system to access the requested url");
        }
    }
}
