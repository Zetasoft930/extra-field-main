package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.CategoryController;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.CategoryService;
import com.fieldright.fr.util.mapper.CategoryMapper;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins= {"*"}, maxAge = 4800, allowCredentials = "true" )
@RestController
@RequestMapping("/api/category/v1")

public class CategoryControllerImpl implements CategoryController {

	
	
	private CategoryMapper categoryMapper;
	
	@Autowired
    private CategoryService categoryService;
	

    @Override
    @GetMapping
    public  Response<Page<CategoriaDTO>> get(Pageable pageable) {
        return categoryService.findAll(pageable);
    }
    
   
}
