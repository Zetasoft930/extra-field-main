package com.fieldright.fr.util.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;

import ma.glasnost.orika.MapperFacade;

@Component
public class MotoristaLojaMapper {

	private MapperFacade facade;

	public MotoristaLoja toMotoristaLoja(MotoristaLojaDTO dto) {
		MotoristaLoja motoristaLoja= new MotoristaLoja();
		motoristaLoja.setId(dto.getId());
		motoristaLoja.setLoja(dto.getLoja());
		motoristaLoja.setMotorista(dto.getMotorista());
		return motoristaLoja;
	}

	public MotoristaLojaDTO fromMotoristaLoja(MotoristaLoja motoristaLoja) {
		MotoristaLojaDTO motoristaLojaDTO= new MotoristaLojaDTO();
		motoristaLojaDTO.setId(motoristaLoja.getId());
		motoristaLojaDTO.setLoja(motoristaLoja.getLoja());
		motoristaLojaDTO.setMotorista(motoristaLoja.getMotorista());
		return motoristaLojaDTO;
	}

	public List<MotoristaLojaDTO> toMotoristaLojaDTOs(List<MotoristaLoja> motoristaLojaList) {
		return facade.mapAsList(motoristaLojaList, MotoristaLojaDTO.class);
	}
}
