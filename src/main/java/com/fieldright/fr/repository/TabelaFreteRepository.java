package com.fieldright.fr.repository;

import com.fieldright.fr.entity.architecture.TabelaFrete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TabelaFreteRepository extends JpaRepository<TabelaFrete, Long> {
}
