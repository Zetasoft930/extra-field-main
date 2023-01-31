package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Entrega;
import com.fieldright.fr.entity.dto.EntregaDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntregaMapper {

    private MapperFacade facade;

    public EntregaMapper() {
        this.facade = new ConfigurableMapper();
    }

    public EntregaDTO toEntregaDTO(Entrega entrega) {
        return facade.map(entrega, EntregaDTO.class);
    }

    public List<EntregaDTO> toEntregaDTOs(List<Entrega> entregaList) {
        return facade.mapAsList(entregaList, EntregaDTO.class);
    }
}
