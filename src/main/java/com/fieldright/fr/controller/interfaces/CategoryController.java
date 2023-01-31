package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Api(
        tags = "Category endpoints",
        description = "Realiza operações com categorias de produto"
)
public interface CategoryController {

    @ApiOperation(
            value = "Get categories",
            notes = "Este endpoint retorna todas as categorias de produtos que o sistema aceita"
    )
    Response<Page<CategoriaDTO>> get(Pageable pageable);
}
