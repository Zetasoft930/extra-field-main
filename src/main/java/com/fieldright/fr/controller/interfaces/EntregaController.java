package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.dto.EntregaDateDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Api(
        tags = "Entrega endpoints",
        description = "Realiza operações dom entregas"
)
public interface EntregaController {

    @ApiOperation(
            value = "Aceitar entrega",
            notes = "Este endpoint deve ser usado pelos entregadores para aceitar a corrida de entrega, \n" +
                    "passando no param apenas o id da entrega"
    )
    Response<EntregaDTO> aceite(long entregaId);

    @ApiOperation(
            value = "Recusar entrega",
            notes = "Esse endpoint deve ser chamado para indicar que o entregador não deseja realizar a entrega informada"
    )
    Response rejeita(long entregaId);

    @ApiOperation(
            value = "Entrega à caminho",
            notes = "Este endpoint deve ser usado pelos entregadores para indicar que a entrega está à caminho, \n" +
                    "passando no param apenas o id da entrega"
    )
    Response<EntregaDTO> aCaminho(long entregaId);

    @ApiOperation(
            value = "Entrega finalizada",
            notes = "Este endpoint deve ser usado pelos entregadores para indicar que a entrega foi finalizada com sucesso, \n" +
                    "passando no param apenas o id da entrega"
    )
    Response<EntregaDTO> finalize(long entregaId);

    @ApiOperation(
            value = "Histórico de entregas",
            notes = "Este endpoint retorna o histórico de entregas do entregador."
    )
    Response<List<EntregaDTO>> historico();

    @ApiOperation(
            value = "Entregas disponíveis",
            notes = "Este endpoint retorna todas as entregas disponíveis no sistema."
    )
    Response<List<EntregaDTO>> findAllDisponivel();
    
    @ApiOperation(value = "Get Entregas by user and date", notes = "Este endpoint deve ser utilizado para recuperar as entregas dos ultimos xxx dias")
	ResponseEntity<Response<Page<EntregaDateDTO>>> getDepoimentosByFilters(int daysAgo, Pageable pageable);
    
    @ApiOperation(value = "Get last Entregas by user", notes = "Este endpoint deve ser utilizado para recuperar as últimas entregas do entregador")
	ResponseEntity<Response<Page<EntregaDateDTO>>> getLastEntregasByUser(Pageable pageable);
}
