package com.fieldright.fr.repository;

import com.fieldright.fr.entity.CidadeAtuacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeAtuacaoRepository extends JpaRepository<CidadeAtuacao, Long> {

    CidadeAtuacao findCidadeAtuacaoByCodigoUFAndCodigoMunicipio(int codigoUF, int codigoMunicipio);

    CidadeAtuacao findCidadeAtuacaoById(Long id);
}
