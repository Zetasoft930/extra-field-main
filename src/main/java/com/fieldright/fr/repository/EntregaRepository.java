package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Entrega;
import com.fieldright.fr.util.enums.StatusEntrega;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findAllByEntregadorId(long entregadorId);

    List<Entrega> findAllByStatusEntregaIs(StatusEntrega status);

    @Query(
                    value = "DELETE FROM usuario_entregas_rejeitadas where entregas_rejeitadas_id = ?1",
                    nativeQuery = true)
    void atualizeEntregasRejeitadas(long entregaId);
    
    @Query(value = "select created_at, sum(valor_remuneracao)  from entrega e where entregador_id = ?1\r\n"
    		+ "and current_date - date(created_at) between 0 and ?2 group by created_at", nativeQuery = true)
	Page<Object[]> findByMotoristaAndDays(long usuarioId, int daysAgo, Pageable pageable);
	
	 @Query(value = "select c.product_id, c.product_name  from entrega e join venda v on e.venda_id = v.id join venda_compras vc on v.id = vc.venda_id \r\n"
	 		+ "join compra c on vc.compras_id = c.id where entregador_id =?1 order by e.created_at desc", nativeQuery = true)
		Page<Object[]> getLastEntregasByUser(long usuarioId, Pageable pageable);
}
