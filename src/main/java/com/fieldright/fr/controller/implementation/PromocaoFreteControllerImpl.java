package com.fieldright.fr.controller.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fieldright.fr.controller.interfaces.PromocaoFreteController;
import com.fieldright.fr.controller.interfaces.PromocaoproductController;
import com.fieldright.fr.controller.interfaces.SuperCategoryController;
import com.fieldright.fr.entity.dto.PromocaoFreteDTO;
import com.fieldright.fr.entity.dto.PromocaoProductDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.PromocaoFreteService;
import com.fieldright.fr.service.interfaces.PromocaoProductService;
import com.fieldright.fr.service.interfaces.SuperCategoryService;
import com.fieldright.fr.util.mapper.PromocaoFreteMapper;
import com.fieldright.fr.util.mapper.PromocaoProductMapper;
import com.fieldright.fr.util.mapper.SuperCategoryMapper;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/promocaoFrete/v1")
@AllArgsConstructor
public class PromocaoFreteControllerImpl implements PromocaoFreteController{
	
	@Autowired
	private PromocaoFreteMapper promocaoFreteMapper;
	
	@Autowired
	private PromocaoFreteService promocaoFreteService;
	
	@Override
	 @PostMapping(
	            value = "/newPromotion"
	    )
	public PromocaoFreteDTO create(@RequestBody PromocaoFreteDTO dto) {
		return promocaoFreteService.save(promocaoFreteMapper.toPromocaoFrete(dto));
	}
	
	
}
