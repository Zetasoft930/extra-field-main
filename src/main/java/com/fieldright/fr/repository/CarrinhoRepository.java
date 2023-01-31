package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.util.enums.StatusCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    List<Carrinho> findAllCarrinhoByStatusCompra(StatusCompra statusCompra);
}
