package com.fieldright.fr.controller.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.PostagemFilterDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Api(
        tags = "Postagem endpoints",
        description = "Realiza operações com Postagem"
)
public interface PostagemController {


    @ApiOperation(value = "save", notes = "Registar nova postagem")
    @ResponseStatus(HttpStatus.CREATED)
    Response save(String data,MultipartFile file) throws JsonProcessingException;

    @ApiOperation(value = "findBystatus", notes = "Lista de Postagem por status")
    @ResponseStatus(HttpStatus.OK)
    Response findBystatus(Integer status, Pageable pageable);

    @ApiOperation(value = "delete", notes = "Eliminar Postagem por id")
    @ResponseStatus(HttpStatus.OK)
    Response delete(Long id);

    @ApiOperation(value = "updateStatus", notes = "Actualizar o status do Postagem")
    @ResponseStatus(HttpStatus.OK)
    Response updateStatus(Long id,Integer status);

    @ApiOperation(value = "findAll", notes = "Lista de Postagem geral")
    @ResponseStatus(HttpStatus.OK)
    Response findAll(Pageable pageable);


}
