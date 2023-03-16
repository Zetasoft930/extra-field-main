package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {


    @Query(value = "select c.* from compra c \n" +
            "\tinner join venda_compras vc on vc.compras_id  = c.id \n" +
            "\tinner join venda v on v.id =vc.venda_id \n" +
            "where\n" +
            "\t v.vendedor_id  = ?1\n" +
            "and v.status = ?2",nativeQuery = true)
    Page<Compra> findVendaByVendedorAndStatus(Long usuarioLojaId, String status, Pageable pageable);

}
