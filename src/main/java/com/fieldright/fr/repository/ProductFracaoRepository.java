package com.fieldright.fr.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fieldright.fr.entity.ProductFracao;

public interface ProductFracaoRepository extends JpaRepository<ProductFracao, Long> {
	
	@Query(value = "select limite_fracao from product_fracao pf where pf.product_id = ?1 order by created_at desc limit 1", nativeQuery = true)
	int findFracaoByProduct(long productId);

}
