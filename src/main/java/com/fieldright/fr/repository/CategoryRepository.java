package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.architecture.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Optional<Category> findByNameIgnoreCase(String name);

    @Query(value = "select * from category c ", nativeQuery = true)
    Page<Category> findAll(Pageable pageable);

}
