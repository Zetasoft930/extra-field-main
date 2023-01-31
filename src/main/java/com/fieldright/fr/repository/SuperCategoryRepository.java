package com.fieldright.fr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fieldright.fr.entity.SuperCategory;

public interface SuperCategoryRepository  extends JpaRepository<SuperCategory, Long> {
	
	 @Query(value = "select * from super_category sc ", nativeQuery = true)
	    Page<SuperCategory> findAll(Pageable pageable);

}
