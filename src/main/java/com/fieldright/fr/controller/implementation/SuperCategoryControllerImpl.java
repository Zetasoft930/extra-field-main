package com.fieldright.fr.controller.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fieldright.fr.controller.interfaces.SuperCategoryController;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.SuperCategoryService;
import com.fieldright.fr.util.mapper.SuperCategoryMapper;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/superCategory/v1")
@AllArgsConstructor
public class SuperCategoryControllerImpl implements SuperCategoryController{
	
	@Autowired
	private SuperCategoryService categoryService;

	@GetMapping
	@Override
	public Response<Page<SuperCategoryDTO>> get(Pageable pageable) {
		return categoryService.findAll(pageable);
	}

}
