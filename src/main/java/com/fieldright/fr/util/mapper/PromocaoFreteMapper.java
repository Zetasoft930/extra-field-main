package com.fieldright.fr.util.mapper;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.PromocaoFrete;
import com.fieldright.fr.entity.dto.PromocaoFreteDTO;

@Component
public class PromocaoFreteMapper {

	public PromocaoFrete toPromocaoFrete(PromocaoFreteDTO dto) {
		return PromocaoFrete.builder().id(dto.getId()).createdAt(dto.getCreatedAt()).endDate(dto.getEndDate())
				.percentage(dto.getPercentage()).lojaId(dto.getLojaId()).startDate(dto.getStartDate()).hide(dto.isHide()).build();
	}

	public PromocaoFreteDTO fromPromocaoFrete(PromocaoFrete entity) {
		PromocaoFreteDTO dto = new PromocaoFreteDTO();
		dto.setId(entity.getId());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setEndDate(entity.getEndDate());
		dto.setPercentage(entity.getPercentage());
		dto.setLojaId(entity.getLojaId());
		dto.setStartDate(entity.getStartDate());
		dto.setHide(entity.isHide());
		return dto;
	}
}
