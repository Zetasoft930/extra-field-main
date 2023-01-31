package com.fieldright.fr.repository;

import com.fieldright.fr.entity.architecture.MoneyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoneyConfigRepository extends JpaRepository<MoneyConfig, Long> {

    Optional<MoneyConfig> findByPaisCodigoLike(String paisCodigo);
}
