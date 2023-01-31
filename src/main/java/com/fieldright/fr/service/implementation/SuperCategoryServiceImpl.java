package com.fieldright.fr.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.repository.SuperCategoryRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.SuperCategoryService;
import com.fieldright.fr.util.mapper.SuperCategoryMapper;

@Service
public class SuperCategoryServiceImpl implements SuperCategoryService {

	@Autowired
	private SuperCategoryRepository categoryRepository;

	@Override
	public List<SuperCategory> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public SuperCategory findById(long id) {
		return categoryRepository.findById(id).get();
	}

	@Override
	public Response<Page<SuperCategoryDTO>> findAll(Pageable pageable) {
		List<SuperCategoryDTO> dtos=null;
    	Page<SuperCategory> all = categoryRepository.findAll(pageable);
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(all)
                .withErrors(null)
                .build();
	}

}
