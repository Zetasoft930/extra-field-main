package com.fieldright.fr.repository;

import com.fieldright.fr.entity.AvaliacaoProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;

@Repository
public interface AvaliacaoProductRepository extends JpaRepository<AvaliacaoProduct, Long> {
	
	@Query(value = "select * from avaliacao_product where product_id =?1 and avaliador_id=?2", nativeQuery = true)
	AvaliacaoProduct findAvaliacaoProductByProductAndAvaliador(long productId, long avaliadorId);


	@Query(value = "select p.id,sum(ap.estrelas)/count(ap.id) estrelas\n" +
			"from avaliacao_product ap " +
			"\tinner join product p on p.id =ap.product_id " +
			"group by p.id " +
			"order by p.name ",nativeQuery = true)
	Page<Object[]> findAvaliacaoAll(Pageable pageable);


	@Query(value = "select p.id,sum(ap.estrelas)/count(ap.id) estrelas\n" +
			"from avaliacao_product ap " +
			"\tinner join product p on p.id =ap.product_id " +
			" where p.id = ?" +
			"group by p.id " +
			"order by p.name ",nativeQuery = true)
	Page<Object[]> findAvaliacaoByProduto(Long productId, Pageable pageable);


	@Query(value = "select u.id,u.first_name ,u.last_name ,u.avatar ,ap.estrelas \n" +
			"from usuario u \n" +
			"\tinner join avaliacao_product ap on ap.avaliador_id  = u.id \n" +
			"where ap.product_id  = ?",nativeQuery = true)
	List<Object[]> findUserByProuct(Long productId);
}
