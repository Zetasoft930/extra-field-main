package com.fieldright.fr.repository;

import com.fieldright.fr.entity.AvaliacaoProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoProductRepository extends JpaRepository<AvaliacaoProduct, Long> {
	
	@Query(value = "select * from avaliacao_product where product_id =?1 and avaliador_id=?2", nativeQuery = true)
	AvaliacaoProduct findAvaliacaoProductByProductAndAvaliador(long productId, long avaliadorId);

}
