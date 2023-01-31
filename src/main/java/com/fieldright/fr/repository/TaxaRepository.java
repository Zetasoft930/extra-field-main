package com.fieldright.fr.repository;

import com.fieldright.fr.entity.architecture.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxaRepository extends JpaRepository<Taxa, Long> {
}
