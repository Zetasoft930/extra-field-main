package com.fieldright.fr.util.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.Depoimento;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.entity.dto.DepoimentoDTO;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class DepoimentoMapper extends GenericMapper<Depoimento, DepoimentoDTO> {
	
	 private MapperFacade facade;

	    public DepoimentoMapper() {
	        this.facade = new ConfigurableMapper();
	    }

	    public DepoimentoDTO toDepoimentoDTO(Depoimento depoimento){
	        return facade.map(depoimento,DepoimentoDTO.class);
	    }
	    
	    public Depoimento fromDepoimentoDTO(DepoimentoDTO depoimentoDTO){
	        return facade.map(depoimentoDTO,Depoimento.class);
	    }

	    public List<CarrinhoDTO> tDepoimentoDTOList(List<Carrinho> carrinhoList){
	        return facade.mapAsList(carrinhoList, CarrinhoDTO.class);
	    }

}
