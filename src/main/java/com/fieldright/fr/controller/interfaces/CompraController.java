package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.entity.dto.CompraDTO;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Api(
        tags = "Compra endpoints",
        description = "Realiza operações com compras"
)
public interface CompraController {

    @ApiOperation(
            value = "New compra",
            notes = "Este endpoint deve ser chamado quando for realizar a compra de produtos. \n" +
                    "Ao chamar este endpoint, serão feitas todas as validações necessárias no back-end para a realização" +
                    " da compra" +
                    " e será retornado um string que seria o códico da transação para montar o link de pagamento com PagSeguro.. \n" +
                    "É passado no body da requisição um **CarrinhoDTO** (sem id), que possui os campos seguintes: \n" +
                    "   * ***id*** : *Não informar* \n" +
                    "   * ***compradorId*** : *Não é necessário informar* \n" +
                    "   * ***compras*** : *Para cada compra, basta informar os seguintes campos* : \n" +
                    "       * ***productId*** : *O id do produto comprado* \n" +
                    "       * ***vlPago*** : *O valor total a ser pago para o produto* \n" +
                    "       * ***qtdComprada*** : *Quantidade de unidades compradas* \n" +
                    "       * ***enderecoEntrega*** : *O endereço de entrega do produto* \n" +
                    "       * ***formaPagamento*** : *A forma de pagamento*>> (*CARTAO_DEBITO/CARTAO_CREDITO*). \n\n\n" +
                    "Também deve ser passado o código ISO alf-3 do país onde se encontra o comprador no parâmetro 'pais'\n\n" +
                    "Caso acontecer algum erro, na maioria dos casos seria o ***406 Not Acceptable***, conferir o campo " +
                    "**errors** do objeto retornado para mais detalhes."
    )
    Response<String> newCompra(CarrinhoDTO carrinho, String pais) throws IOException, JAXBException;

    Response<BigDecimal> calculeFrete(CarrinhoDTO carrinho);

    @ApiOperation(
            value = "Find all",
            notes = "Este endpoint retorna todas as compras realizadas pelo comprador, passando apenas o token deste."
    )
    Response<List<CarrinhoDTO>> findAll();

    Response<HttpStatus> evaluate(Avaliacao avaliacao, long lojaId);
    
    Response<BigDecimal> price(CompraDTO dto);
}
