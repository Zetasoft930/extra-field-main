package com.fieldright.fr.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;

public interface SuperCategoryService {
	List<SuperCategory> findAll();
	
	SuperCategory findById(long id);
	
	 Response<Page<SuperCategoryDTO>> findAll(Pageable pageable);
}
