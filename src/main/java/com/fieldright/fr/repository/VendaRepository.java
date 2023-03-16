package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.util.enums.StatusVenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByVendedorIdAndStatusEquals(Long vendedorId, StatusVenda status);

    List<Venda> findAllByCarrinhoId(long carrinhoId);

    //NOVO CODIGO
    @Query(value = "select" +
            "       COUNT(v.id) from  venda v \n" +
            "\tinner join usuario u on u.id  = v.vendedor_id  \n" +
            "where \n" +
            "\tv.status =?2\n" +
            "AND\tv.vendedor_id  = ?1\n" +
            "group by u.id", nativeQuery = true)
    Optional<BigDecimal> countVendaByVendedorAndStatus(long usuarioId, String status);



    @Query(value = "select" +
            "       SUM(v.vl_total) from  venda v \n" +
            "\tinner join usuario u on u.id  = v.vendedor_id  \n" +
            "where \n" +
            "\tv.status =?2\n" +
            "AND\tv.vendedor_id  = ?1\n" +
            "group by u.id", nativeQuery = true)
    Optional<BigDecimal> totalValorByVendedorAndStatus(long usuarioId, String status);


}
