package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.architecture.UnidadeMedida;
import com.fieldright.fr.repository.UnidadeMedidaRepository;
import com.fieldright.fr.service.interfaces.UnidadeMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UnidadeMedidaServiceImpl implements UnidadeMedidaService {

    @Autowired
    private UnidadeMedidaRepository repository;

    @Override public List<String> findAll() {
        List<String> unidades = new ArrayList<>();
        Iterable<UnidadeMedida> unidadeMedidas = repository.findAll(
                        Sort.by(Arrays.asList(new Sort.Order(Sort.Direction.ASC, "texto")))
        );
        for (UnidadeMedida unidadeMedida : unidadeMedidas)
            unidades.add(unidadeMedida.toString());

        return unidades;
    }

    @Override
    public boolean isValidUnidadeMedida(String simbolo) {
        Optional<UnidadeMedida> optionalUnidadeMedida = repository.findBySimbolo(simbolo);
        return optionalUnidadeMedida.isPresent();
    }
}
