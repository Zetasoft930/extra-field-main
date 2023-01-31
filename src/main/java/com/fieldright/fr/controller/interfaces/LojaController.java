package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.Loja;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.pagination.Paginacao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Api(
        tags = "Lojas Endpoints",
        description = "Realizar operações com lojas e seu produtos"
)
public interface LojaController {

    @ApiOperation(
            value = "Get lojas by Name",
            notes = "Este endpoint deve ser utilizado para recuperar as lojas cadastradas no s" +
                    "istema pelo nome. Deve ser passado o parâmetro 'name' que seria o nome procurado"
    )
    ResponseEntity<Response<Page<Loja>>> getLojas(String name, long category, Pageable pageable);

    @ApiOperation(
            value = "Get products",
            notes = "Endpoint que retorna os produtos de uma loja, com paginação."
    )
    Response<Page<ProductDTO>> getProducts(@ApiParam(value = "id da loja") Long lojaId,
                                           @ApiParam(value = "nome do produto") String name,
                                           @ApiParam(value = "categoria do produto") String category,
                                           Paginacao paginacao);

    @ApiOperation(
            value = "Get products",
            notes = "Endpoint que retorna os produtos, com paginação e filtros."
)
ResponseEntity<Response<Page<ProductDTO>>> getProductsByFilters(long loja, LocalDate searchDate, String category, String name, BigDecimal price,boolean maisVendido, String status, Pageable pageable);
}
