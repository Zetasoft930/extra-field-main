package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(
        tags = "Unidade Medida Converter endpoints",
        description = "Realiza operações com conversao de unidade de medida"
)
public interface UnidadeMedidaConverterController {


    @ApiOperation(value = "save", notes = "Registar nova Equivalencia de unidade")
    @ResponseStatus(HttpStatus.CREATED)
    Response save(UnidadeMedidaConverterDTO postagemDTO);

    @ApiOperation(value = "update", notes = "Actualizar  Equivalencia de unidade")
    @ResponseStatus(HttpStatus.OK)
    Response update(Long id,UnidadeMedidaConverterDTO postagemDTO);

    @ApiOperation(value = "findAll", notes = "Lista de Equivalencia de unidade")
    @ResponseStatus(HttpStatus.OK)
    Response findAll(Pageable pageable);

    @ApiOperation(value = "delete", notes = "Eliminar Equivalencia de unidade por id")
    @ResponseStatus(HttpStatus.OK)
    Response delete(Long id);



}
