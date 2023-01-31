package com.fieldright.fr.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.response.Response;

public interface CategoryService {

    List<String> internalFindAll();
    
    Response<Page<CategoriaDTO>> findAll(Pageable pageable);

    void internalValidCategory(String category);

    boolean isValidCategory(String category);
}
