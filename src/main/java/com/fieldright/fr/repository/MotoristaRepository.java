package com.fieldright.fr.repository;

import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.util.enums.TipoVeiculo;

import java.util.List;

public interface MotoristaRepository extends UserRepository<Motorista> {

    List<Usuario> findAllByTipoVeiculoIsLike(TipoVeiculo veiculo);

    List<Usuario> findAllByTipoVeiculoInAndCidadeAtuacaoIn(List<TipoVeiculo> veiculos, String... cidades);
}
