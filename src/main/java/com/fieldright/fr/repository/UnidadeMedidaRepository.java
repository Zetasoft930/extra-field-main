package com.fieldright.fr.repository;

import com.fieldright.fr.entity.architecture.UnidadeMedida;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeMedidaRepository extends PagingAndSortingRepository<UnidadeMedida, Long> {
    Optional<UnidadeMedida> findBySimbolo(String simbolo);
}
