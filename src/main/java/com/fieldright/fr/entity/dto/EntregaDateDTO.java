package com.fieldright.fr.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntregaDateDTO {
	
	private String date;
	private BigDecimal value;
	private String productName;
	private List<String> pictures;
	private String loja;
	private String depoimento;
	private byte estrelas;
}
