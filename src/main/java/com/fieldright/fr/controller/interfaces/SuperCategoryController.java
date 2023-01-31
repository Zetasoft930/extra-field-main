package com.fieldright.fr.controller.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;

import io.swagger.annotations.ApiOperation;

public interface SuperCategoryController {
	 
	 @ApiOperation(
	            value = "Get categories lojas",
	            notes = "Este endpoint retorna todas as categorias de lojas que o sistema aceita"
	    )
	    Response<Page<SuperCategoryDTO>> get(Pageable pageable);


}
