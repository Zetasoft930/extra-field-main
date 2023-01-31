package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.dto.ContaDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class ContaMapper {

    private MapperFacade facade;

    public ContaMapper() {
        this.facade = new ConfigurableMapper();
    }

    public ContaDTO toContaDTO(Conta conta) {
        return facade.map(conta, ContaDTO.class);
    }

    public Conta toConta(ContaDTO dto) {
        return facade.map(dto, Conta.class);
    }
}
