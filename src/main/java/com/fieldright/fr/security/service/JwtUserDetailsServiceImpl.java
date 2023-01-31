package com.fieldright.fr.security.service;

import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.JwtUser;
import com.fieldright.fr.security.JwtUserFactory;
import com.fieldright.fr.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return loadUserByEmail(email);
    }
    public JwtUser loadUserByUsername2(String email) {
        return loadUserByEmail2(email);
    }

    private UserDetails loadUserByEmail(String email) {
        UserDTO user = userService.internalFindUserByEmail(email);
        return JwtUserFactory.create(user);
    }

    private JwtUser loadUserByEmail2(String email) {
        UserDTO user = userService.internalFindUserByEmail(email);
        return JwtUserFactory.create(user);
    }
}
