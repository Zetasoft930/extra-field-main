package com.fieldright.fr.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.fieldright.fr.entity.PromocaoProduct;

public interface PromocaoProductRepository extends PagingAndSortingRepository<PromocaoProduct, Long> {
	
	@Query(value = "select * from promocao_product pf where current_date between start_date and end_date and product_id  = ?1", nativeQuery = true)
	PromocaoProduct findproductPromotion(long product);

	
}
