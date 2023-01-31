package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.util.form.SingUpForm;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private MapperFacade facade;

    public UserMapper() {
        facade = new ConfigurableMapper();
    }

    public UserDTO userDTOFrom(Usuario usuario) {
        return facade.map(usuario, UserDTO.class);
    }

    public List<UserDTO> toUserDTOList(List<Usuario> usuarioList) {
        List<UserDTO> userDTOS = facade.mapAsList(usuarioList, UserDTO.class);
        removePasswords(userDTOS);
        return userDTOS;
    }

    private void removePasswords(List<UserDTO> userDTOS) {
        for (UserDTO dto : userDTOS)
            dto.setPassword(null);
    }

    public Comprador compradorFrom(SingUpForm form) {
        return facade.map(form, Comprador.class);
    }

    public Vendedor vendedorFrom(SingUpForm form) {
        return facade.map(form, Vendedor.class);
    }

    public Vendedor vendedorFrom(UserDTO userDTO) {
        return facade.map(userDTO, Vendedor.class);
    }

    public Motorista motoristaFrom(SingUpForm form) {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFactory.classMap(Motorista.class, SingUpForm.class)
                .exclude("cidadeAtuacao")
                .byDefault()
                .register();

        form.setTipoVeiculo(form.getTipoVeiculo().toUpperCase());
        return mapperFactory.getMapperFacade().map(form, Motorista.class);
    }
}
