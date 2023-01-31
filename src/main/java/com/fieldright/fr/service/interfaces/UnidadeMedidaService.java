package com.fieldright.fr.service.interfaces;

import java.util.List;

public interface UnidadeMedidaService {

    List<String> findAll();

    boolean isValidUnidadeMedida(String simbolo);
}
