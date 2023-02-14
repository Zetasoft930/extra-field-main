package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;

import java.util.List;

@Api(
        tags = "Venda endpoints",
        description = "Manipulação de vendas"
)
public interface VendaController {

    @ApiOperation(
            value = "Confirm venda",
            notes = "Este endpoint deve ser chamado pelo vendedor para confirmar uma venda com status **NOVA**, " +
                    "passando no path o id da venda.\n" +
                    "Caso a venda informada não estiver com status **NOVA** ou o usuário logado não possuir permissão " +
                    "para confirmar a venda informada, o retorno será um ***Bad Request*** com mensagem de: " +
                    "***Não foi possível confirmar a venda informada***"
    )
    Response<HttpStatus> confirm(long id);

    @ApiOperation(
            value = "Cancel venda",
            notes = "Este endpoint deve ser chamado pelo vendedor para cancelar uma venda com status **NOVA**, " +
                    "passando no path o id da venda.\n" +
                    "Caso a venda informada não estiver com status **NOVA** ou o usuário logado não possuir permissão " +
                    "para cancelar a venda informada, o retorno será um ***Bad Request*** com mensagem de: " +
                    "***Não foi possível cancelar a venda informada***"
    )
    Response<HttpStatus> cancel(long id);

    @ApiOperation(
            value = "Venda ready",
            notes = "Este endpoint deve ser chamado para confirmar a preparação do produto comprado, passando em " +
                    "parâmetro o id da venda.\n" +
                    "Caso a venda informada não estiver com status **EM_PREPARACAO** ou o usuário logado não possuir permissão " +
                    "para cancelar a venda informada, o retorno será um ***Bad Request*** com mensagem de: " +
                    "***Não foi possível realizar a ação solicitada***\""
    )
    Response<VendaDTO> ready(long id);

    @ApiOperation(
            value = "Find all",
            notes = "Este endpoint retorna a lista de vendas de um vendedor, passando apenas o token do usuário logado"
    )
    Response<List<VendaDTO>> findAll();

    @ApiOperation(
            value = "Entrega a caminho",
            notes = "Esse endpoint deve ser chamado pelas lojas que possuem seus próprios entregadores, " +
                    "para informar que o pedido já está a caminho para o endereço do comprador."
    )
    Response pedidoACaminho(Long vendaId);

    @ApiOperation(
            value = "Pedido entregue",
            notes = "Esse endpoint deve ser chamado pelas lojas que possuem seus próprios entregadores," +
                    "para informar que o pedido já foi entregue para o endereço do comprador."
    )
    Response pedidoFinalizado(Long vendaId);

//NOVO CODIGO
    @ApiOperation(
            value = "total de venda",
            notes = "Esse endpoint serve para mostrar o total de vendas de um usuario unico/loja. deve ser informado o id do usuario e status da venda."
    )
    Response countVendaByVendedorAndStatus(Long userIdLoja,String status);
}
