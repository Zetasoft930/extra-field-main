package com.fieldright.fr.util.mapper;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class ProductAvaliacaoMapper {

	private MapperFacade facade;
	
	  public ProductAvaliacaoMapper() {
	        this.facade = new ConfigurableMapper();
	    }
	
	 public AvaliacaoProduct toProductAvaliacao(AvaliacaoProductDTO dto){
	        return facade.map(dto, AvaliacaoProduct.class);
	    }

	    public AvaliacaoProductDTO toProductAvaliacaoDTO(AvaliacaoProduct avaliacaoProduct){
	        return facade.map(avaliacaoProduct, AvaliacaoProductDTO.class);
	    }
}
