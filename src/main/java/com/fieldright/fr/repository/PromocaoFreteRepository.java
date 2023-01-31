package com.fieldright.fr.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.fieldright.fr.entity.PromocaoFrete;

public interface PromocaoFreteRepository extends PagingAndSortingRepository<PromocaoFrete, Long> {
	
	@Query(value = "select percentage from promocao_frete pf where current_date between start_date and end_date and loja_id = 0 or loja_id = ?1", nativeQuery = true)
	BigDecimal findFretePromotion(long loja);

}
