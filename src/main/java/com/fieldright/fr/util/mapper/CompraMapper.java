package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.dto.CompraDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompraMapper {
    private MapperFacade facade;

    public CompraMapper() {
        this.facade = new ConfigurableMapper();
    }

    public List<CompraDTO> toCompraDTOS(List<Compra> compraList) {
        return facade.mapAsList(compraList, CompraDTO.class);
    }
}
