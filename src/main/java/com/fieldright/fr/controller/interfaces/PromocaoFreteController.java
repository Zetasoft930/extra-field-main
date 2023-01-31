package com.fieldright.fr.controller.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fieldright.fr.entity.dto.PromocaoFreteDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;

import io.swagger.annotations.ApiOperation;

public interface PromocaoFreteController {
	
	 @ApiOperation(
	            value = "PromocaoFrete",
	            notes = ""
	    )
	    @ResponseStatus(HttpStatus.CREATED)
	 PromocaoFreteDTO create(PromocaoFreteDTO dto);


}
