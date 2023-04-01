package com.fieldright.fr.repository;

import com.fieldright.fr.entity.CategoriaPostagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaPostagemRepository extends JpaRepository<CategoriaPostagem,Long> {

    @Query(value = "SELECT * FROM CategoriaPostagem p WHERE p.status = ?1 ORDER BY p.name",nativeQuery = true)
    public Page<CategoriaPostagem> findByStatus(String status, Pageable pageable);
}
