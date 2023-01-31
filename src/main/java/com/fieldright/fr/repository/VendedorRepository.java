package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Vendedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendedorRepository extends PagingAndSortingRepository<Vendedor, Long> {

    @Query(value = "select u " +
                    "from Usuario as u " +
                    "       where " +
                    "           u.perfil = 'vendedor' " +
                    "       and u.active = true" +
                    "       and (" +
                    "               LOWER(u.firstName) like %?1% " +
                    "            or LOWER(u.lastName) like %?1%" +
                    "           ) and (categoria = ?2 or ?2 = 0)"
    )
    Page<Vendedor> findByName(String name, long category, Pageable pageable);


    @Query(value = "SELECT u FROM Vendedor INNER JOIN ",nativeQuery = true)
    public List<Vendedor> findTotalVenda();
}
