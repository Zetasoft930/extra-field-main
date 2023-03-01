package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByUsuarioId(long id);

    @Query(value = "select c.* from conta c\n" +
            "\tinner join usuario u on u.id  = c.usuario_id \n" +
            "where u.dtype ='Motorista'\n" +
            "and c.id not in\n" +
            "(\n" +
            "\tselect hp.conta_id \n" +
            "\tfrom historico_pagamento hp \n" +
            "\twhere date(hp.created_at) = current_date \n" +
            ")",nativeQuery = true)
    List<Conta> findMotoristaNotInHistoricoPagamentoToday();
}
