package com.fieldright.fr.entity.dto;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

import lombok.Data;

@Data
public class CategoriaDTO {
	
	private long id;
	private String name;
	private long SuperCategoryId;
    private List<String> pictures;

}
