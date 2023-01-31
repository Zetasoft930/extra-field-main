package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.util.enums.StatusVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByVendedorIdAndStatusEquals(Long vendedorId, StatusVenda status);

    List<Venda> findAllByCarrinhoId(long carrinhoId);


}
