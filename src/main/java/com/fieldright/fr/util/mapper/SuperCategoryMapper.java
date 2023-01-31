package com.fieldright.fr.util.mapper;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;

@Component
public class SuperCategoryMapper {

	public SuperCategory toSuperCategoria(SuperCategoryDTO dto) {
		return SuperCategory.builder().nome(dto.getNome()).pictures(dto.getPictures()).build();
	}

	public SuperCategoryDTO fromSuperCategoria(SuperCategory entity) {
		SuperCategoryDTO dto = new SuperCategoryDTO();
		dto.setId(entity.getId());
		dto.setNome(entity.getNome());
		return dto;
	}
}
