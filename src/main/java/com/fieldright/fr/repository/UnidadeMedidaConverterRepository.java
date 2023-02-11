package com.fieldright.fr.repository;

import com.fieldright.fr.entity.UnidadeMedidaConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeMedidaConverterRepository extends JpaRepository<UnidadeMedidaConverter,Long> {

    @Query("SELECT u FROM UnidadeMedidaConverter u INNER JOIN u.unidadeMedida_origem uo WHERE uo.simbolo=?1")
    public Optional<UnidadeMedidaConverter> findByUnidadeOrigem(String unidade);

    @Query("SELECT u FROM UnidadeMedidaConverter u INNER JOIN u.unidadeMedida_origem uo WHERE uo.simbolo=?1 and u.unidadeMedida_destino.simbolo=?2")
    public Optional<UnidadeMedidaConverter> findByUnidadeOrigem(String unidadeOrigem,String unidadeDestino);
}
