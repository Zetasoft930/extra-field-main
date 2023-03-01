package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.HistoricoPagamento;
import com.fieldright.fr.util.enums.StatusHistoricoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface HistoricoPagamentoRepository extends JpaRepository<HistoricoPagamento,Long> {


    @Query(value = "SELECT p FROM HistoricoPagamento p WHERE p.status = ?1 AND p.conta=?2")
    Page<HistoricoPagamento> findByStatusAndConta(StatusHistoricoPagamento status, Conta conta, Pageable pageable);

    @Query(value = "SELECT p FROM HistoricoPagamento p WHERE p.transferCode=?1")
    Optional<HistoricoPagamento> findByStatusAndTransfereCode( String transferCode);

    @Query(value = "select \n" +
            "   \t\thp.valor montante,\n" +
            "   \t\thp.created_at data,\n" +
            "        hp.status  \t\t\n" +
            "   \tfrom historico_pagamento hp\n" +
            "   \t\tinner join conta c on c.id  = hp.conta_id \n" +
            "   \t\tinner join usuario u on u.id = c.usuario_id\n" +
            "   \twhere\n" +
            "   \t\tu.id = ?1\n and u.dtype = ?2" +
            "   \torder by DATE(hp.created_at) DESC",nativeQuery = true)
    Page<Object[]> findPagamentoByUsuario(Long id,String tipoUsuario,Pageable pageable);

    @Query(value = "select \n" +
            "   \t\thp.valor montante,\n" +
            "   \t\thp.created_at data,\n" +
            "        hp.status  \t\t\n" +
            "   \tfrom historico_pagamento hp\n" +
            "   \t\tinner join conta c on c.id  = hp.conta_id \n" +
            "   \t\tinner join usuario u on u.id = c.usuario_id\n" +
            "   \twhere\n" +
            "   \t\tu.id = ?1\n and u.dtype = ?2 AND hp.created_at BETWEEN ?3 AND ?4 " +
            "   \torder by DATE(hp.created_at) DESC",nativeQuery = true)
    Page<Object[]> findPagamentoByUsuario(Long id, String text, Timestamp dataInicio, Timestamp dataTermino, Pageable pageable);

   /* @Query(value = "select \n" +
            "   \t\tSUM(hp.valor) montante,\n" +
            "   \t\tDATE(hp.created_at) data,\n" +
            "        hp.status  \t\t\n" +
            "   \tfrom historico_pagamento hp\n" +
            "   \t\tinner join conta c on c.id  = hp.conta_id \n" +
            "   \t\tinner join usuario u on u.id = c.usuario_id\n" +
            "   \twhere\n" +
            "   \t\tu.id = ?1\n and u.dtype = ?2" +
            "   \tgroup by DATE(hp.created_at),hp.status,hp.conta_id\n" +
            "   \torder by DATE(hp.created_at) DESC",nativeQuery = true)
    Page<Object[]> findPagamentoByUsuario(Long id,String tipoUsuario,Pageable pageable);*/

}
