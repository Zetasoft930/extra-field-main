package com.fieldright.fr.repository;

import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemuneracaoEntregadoresConfigRepository extends JpaRepository<RemuneracaoEntregadoresConfig, Long> {
}
