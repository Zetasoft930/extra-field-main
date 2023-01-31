package com.fieldright.fr.controller.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.response.Response;

import io.swagger.annotations.ApiOperation;

public interface DepoimentoController {

	 @ApiOperation(
	            value = "Depoimento",
	            notes = ""
	    )
	    @ResponseStatus(HttpStatus.CREATED)
	 DepoimentoDTO create(DepoimentoDTO dto);
	 
	 
		@ApiOperation(value = "Get Depoimentos by user, status or date", notes = "Este endpoint deve ser utilizado para recuperar os depoimentos")
		ResponseEntity<Response<Page<DepoimentoDTO>>> getDepoimentosByFilters(long usuarioId, String status,
				LocalDate startdate, LocalDate endDate, Pageable pageable);
	}
