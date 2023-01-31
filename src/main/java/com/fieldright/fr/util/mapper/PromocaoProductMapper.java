package com.fieldright.fr.util.mapper;

import org.springframework.stereotype.Component;

import com.fieldright.fr.entity.PromocaoProduct;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;

@Component
public class PromocaoProductMapper {

	public PromocaoProduct toPromocaoProduct(PromocaoProductDTO dto) {
		PromocaoProduct promocaoProduct = new PromocaoProduct();
		promocaoProduct.setId(dto.getId());
		promocaoProduct.setCreatedAt(dto.getCreatedAt());
		promocaoProduct.setEndDate(dto.getEndDate());
		promocaoProduct.setPercentage(dto.getPercentage());
		promocaoProduct.setProductId(dto.getProductId());
		promocaoProduct.setStartDate(dto.getStartDate());
		return promocaoProduct;
	}

	public PromocaoProductDTO fromPromocaoProduct(PromocaoProduct entity) {
		PromocaoProductDTO dto = new PromocaoProductDTO();
		dto.setId(entity.getId());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setEndDate(entity.getEndDate());
		dto.setPercentage(entity.getPercentage());
		dto.setProductId(entity.getProductId());
		dto.setStartDate(entity.getStartDate());
		return dto;
	}
}
