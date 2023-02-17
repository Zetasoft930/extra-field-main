package com.fieldright.fr.util.mapper;

import com.fieldright.fr.entity.UnidadeMedidaConverter;
import com.fieldright.fr.entity.architecture.UnidadeMedida;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnidadeMedidaConverterMapper {


    private MapperFacade facade;

    public UnidadeMedidaConverterMapper() {
        this.facade = new ConfigurableMapper();
    }

    public UnidadeMedidaConverter toUnidadeMedidaConverter(UnidadeMedidaConverterDTO dto){

        UnidadeMedidaConverter entity = new UnidadeMedidaConverter();
        entity.setEquivale(dto.getEquivale());
        entity.setUnidadeMedida_destino(new UnidadeMedida(dto.getUnidadeMedidaDestinoId()));
        entity.setUnidadeMedida_origem(new UnidadeMedida(dto.getUnidadeMedidaOrigemId()));

        return entity;
    }

    public void toUnidadeMedidaConverter(UnidadeMedidaConverterDTO dto,UnidadeMedidaConverter entity){


        entity.setEquivale(dto.getEquivale());
        entity.setUnidadeMedida_destino(new UnidadeMedida(dto.getUnidadeMedidaDestinoId()));
        entity.setUnidadeMedida_origem(new UnidadeMedida(dto.getUnidadeMedidaOrigemId()));

    }

    public UnidadeMedidaConverterDTO toUnidadeMedidaConverterDTO(UnidadeMedidaConverter UnidadeMedidaConverter){


        UnidadeMedidaConverterDTO dto = new UnidadeMedidaConverterDTO();
        dto.setEquivale(UnidadeMedidaConverter.getEquivale());
        dto.setId(UnidadeMedidaConverter.getId());
        dto.setUnidadeMedidaDestinoId(UnidadeMedidaConverter.getUnidadeMedida_destino().getId());
        dto.setUnidadeMedidaOrigemId(UnidadeMedidaConverter.getUnidadeMedida_origem().getId());

        return dto;
    }

    public List<UnidadeMedidaConverterDTO> toUnidadeMedidaConverterDTOS(List<UnidadeMedidaConverter> UnidadeMedidaConverterList) {
        return facade.mapAsList(UnidadeMedidaConverterList, UnidadeMedidaConverterDTO.class);
    }
}
