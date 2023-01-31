package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(
                tags = "Unidades de Medidas",
                description = "Endpoints para recuperar unidades de medidas dos produtos"
)
public interface UnidadeMedidaController {

    @ApiOperation(
                    value = "Get unidades de medidas",
                    notes = "Este endpoint retorna todas as unidades de medidas disponíveis no sistema"
    )
    Response<List<String>> get();
}
