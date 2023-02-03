package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository<T extends Usuario> extends JpaRepository<T, Long> {

    Usuario findById(long id);

    Optional<Usuario> findUserByEmailIgnoreCase(String email);


    @Query(value = "select u.* ,\n" +
            "       COUNT(v.id) purchaseTotal " +
            " from  venda v \n" +
            "\tinner join usuario u on u.id  = v.comprador_id  \n" +
            "where \n" +
            "\tv.status = ?2\n" +
            "AND\tv.vendedor_id  = ?1\n" +
            "group by u.id \n" +
            "order by purchaseTotal DESC", nativeQuery = true)
    Page<Object[]> findByFilters(long usuarioId, String status, Pageable pageable);

    @Query(value = "select" +
            "       SUM(v.vl_total) from  venda v \n" +
            "\tinner join usuario u on u.id  = v.vendedor_id  \n" +
            "where \n" +
            "\tv.status =?2\n" +
            "AND\tv.vendedor_id  = ?1\n" +
            "group by u.id", nativeQuery = true)
    Optional<BigDecimal> findCountVendaByVendedor(long usuarioId, String status);
}
