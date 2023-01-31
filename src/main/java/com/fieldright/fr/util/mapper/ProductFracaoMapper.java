package com.fieldright.fr.util.mapper;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.ProductFracao;
import com.fieldright.fr.entity.dto.ProductFracaoDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class ProductFracaoMapper {
	
	private MapperFacade facade;

    public ProductFracaoMapper() {
        this.facade = new ConfigurableMapper();
    }

    public ProductFracao toProductFracao(ProductFracaoDTO dto){
        return facade.map(dto, ProductFracao.class);
    }

    public ProductFracaoDTO toProductFracaoDTO(ProductFracao product){
        return facade.map(product, ProductFracaoDTO.class);
    }
}
