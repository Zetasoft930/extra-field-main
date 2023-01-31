package com.fieldright.fr.entity.dto;


import java.util.List;

import lombok.Data;

@Data
public class SuperCategoryDTO {
	private long id;
	private String nome;
	private List<String> pictures; 
}
