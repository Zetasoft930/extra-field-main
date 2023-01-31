package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.repository.CategoryRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.CategoryService;
import com.fieldright.fr.util.exception.CategoryNotExistException;
import com.fieldright.fr.util.mapper.CategoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    private CategoryMapper categoryMapper;

    @Override
    public List<String> internalFindAll() {
        List<String> categories = new ArrayList<>();
        List<Category> all = categoryRepository.findAll();
        for (Category category : all)
            categories.add(category.getName());

        return categories;
    }
    
    @Override
    public  Response<Page<CategoriaDTO>> findAll(Pageable pageable) {
    	List<CategoriaDTO> dtos=null;
    	List<String> categories = new ArrayList<>();
    	Page<Category> all = categoryRepository.findAll(pageable);
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(all)
                .withErrors(null)
                .build();
    }

    @Override
    public void internalValidCategory(String category) {
        Optional<Category> optional = categoryRepository.findByName(category);
        if (!optional.isPresent())
            throw new CategoryNotExistException();
    }

    @Override
    public boolean isValidCategory(String category) {
        Optional<Category> optional = categoryRepository.findByName(category);
        return optional.isPresent();
    }
}
