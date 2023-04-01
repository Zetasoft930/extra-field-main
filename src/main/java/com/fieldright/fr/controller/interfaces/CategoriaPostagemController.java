package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.CategoriaPostagemDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(
        tags = "Categoria de Postagem endpoints",
        description = ""
)
public interface CategoriaPostagemController {


    @ApiOperation(value = "save", notes = "Registar nova Categoria de Postagem")
    @ResponseStatus(HttpStatus.CREATED)
    Response save(CategoriaPostagemDTO CategoriaPostagemDTO);

    @ApiOperation(value = "findBystatus", notes = "Lista de Categoria de Postagem por status")
    @ResponseStatus(HttpStatus.OK)
    Response findBystatus(Integer status, Pageable pageable);

    @ApiOperation(value = "delete", notes = "Eliminar Categoria de Postagem por id")
    @ResponseStatus(HttpStatus.OK)
    Response delete(Long id);

    @ApiOperation(value = "updateStatus", notes = "Actualizar o status do Categoria de Postagem")
    @ResponseStatus(HttpStatus.OK)
    Response updateStatus(Long id,Integer status);

    @ApiOperation(value = "findAll", notes = "Lista de Categoria de Postagem geral")
    @ResponseStatus(HttpStatus.OK)
    Response findAll(Pageable pageable);


}
