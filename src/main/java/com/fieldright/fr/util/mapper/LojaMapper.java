package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.dto.Loja;
import org.springframework.stereotype.Component;

@Component
public class LojaMapper {

    public Loja toLoja(Vendedor vendedor) {
        Loja loja = new Loja();
        loja.setId(vendedor.getId());
        loja.setNomeLoja(vendedor.getFullName());
        loja.setAvatar(vendedor.getAvatar());

        return loja;
    }

}
