package com.fieldright.fr.controller.interfaces;


import java.util.List;

import com.fieldright.fr.entity.dto.HistoricoPagMotoristaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fieldright.fr.entity.MotoristaLoja;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.dto.MotoristaLojaDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(
        tags = "MotoristaLoja endpoints",
        description = "Realiza operações de associação entre Motoristas e Lojas"
)
public interface MotoristaLojaController {
	
	 @ApiOperation(
	            value = "Associate"
	    )
	  @ResponseStatus(HttpStatus.CREATED)
	    ResponseEntity<Response<HttpStatus>> create(MotoristaLojaDTO motoristaLojaDTO);
	 
	    @ApiOperation(
	            value = "Find all motoristas by loja"
	         )
	    List<MotoristaLoja> findAllByLojas(long loja);

	    @ApiOperation(
	            value = "Find all lojas by motorista"
	         )
	    List<MotoristaLoja> findAllByMotorista(long motorista);
	    
	    List<MotoristaLoja> deleteMotoristaLoja(long id);

	@ApiOperation(
			value = "Historico de Pagamento by motorista"
	)
	Response findPagamentoByMotorista(HistoricoPagMotoristaDTO dto, Pageable pageable);

}
