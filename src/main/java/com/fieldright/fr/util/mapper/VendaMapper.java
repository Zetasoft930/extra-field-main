package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.dto.VendaDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VendaMapper {
    private MapperFacade facade;

    public VendaMapper() {
        this.facade = new ConfigurableMapper();
    }

    public VendaDTO toVendaDTO(Venda venda) {
        return facade.map(venda, VendaDTO.class);
    }

    public List<VendaDTO> toVendaDTOs(List<Venda> vendaList) {
        return facade.mapAsList(vendaList, VendaDTO.class);
    }
}
