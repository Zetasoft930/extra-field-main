package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.entity.dto.*;
import com.fieldright.fr.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Api(
        tags = "Admin Endpoints",
        description = "Realizar operações de administrador"
)
public interface AdminController {

    Response<List<UserDTO>> findAllUser();

    Response<List<UserDTO>> createVendedor(UserDTO vendedor);

    Response<List<UserDTO>> ativeUsuario(long userId);

    Response<List<UserDTO>> desativeUsuario(long userId);

    Response<List<ProductDTO>> findAllProduct();

    Response<List<ProductDTO>> deleteProduct(long productId);

    Response<List<Category>> addCategory(String categoryName);

    Response<List<Category>> deleteCategory(String categoryName);

    Response<List<VendaDTO>> findAllVenda();

    Response<List<CompraDTO>> findAllCompra();

    Response<List<CompraDTO>> alterStatusCompra(long compraId, String newStatus);

    Response<List<Avaliacao>> findAllAvaliacao();

    Response<List<EntregaDTO>> findAllEntrega();

    @ApiOperation(
            value = "Find All MoneyConfig",
            notes = "Esse endpoint deve ser chamado para ver os valores mínimos das compras para cada país."
    )
    Response<List<MoneyConfig>> findAllMoneyConfig();

    @ApiOperation(
            value = "Edit MoneyConfig",
            notes = "Esse endpoint deve ser chamado para alterar o valor mínimo de compra para algum país. \n" +
                    "Basta passar o id da configuração e o novo valor mínimo a ser considerado"
    )
    Response<MoneyConfig> editMoneyConfig(long id, double valorMínimo);

    @ApiOperation(
            value = "Find All MoneyConfig Delivery",
            notes = "Esse endpoint deve ser chamado para ver todas as informações da remuneração dos entregadores."
    )
    Response<List<RemuneracaoEntregadoresConfig>> findAllMoneyConfigDelivery();

    @ApiOperation(
            value = "Edit MoneyConfig Delivery",
            notes = "Esse endpoint deve ser chamado para alterar as informações da remuneração dos entregadores. \n" +
                    "Basta passar as informações a serem consideradas"
    )
    Response<RemuneracaoEntregadoresConfig> editMoneyConfigDelivery(RemuneracaoEntregadoresConfig config);

    @ApiOperation(
            value = "Edit Taxa",
            notes = "Esse endpoint deve ser chamado para alterar as taxas que os vendedores pagam. \n" +
                    "Basta passar o id da taxa e o novo percentual a ser considerado"
    )
    Response<Taxa> editTaxa(long id, double percentual);

    @ApiOperation(
            value = "Pay",
            notes = "Esse endpoint deve ser chamado ao realizar pagamento do vendedor ou do motorista."
    )
    Response<HttpStatus> pay(long userId);

    void canceleComprasNaoPagas();
    
    @ApiOperation(value = "Aprovar depoimento", notes = "")
	Response aprovarDepoimento(long depoimentoId);
    
    @ApiOperation(
            value = "Category",
            notes = "Este endpoint deve ser chamado para realizar um novo cadastro de usuário. \n" +
                    "Deve ser passado no body o formulário **SingUpForm** com os campos necessários para cada tipo de usuários. \n" +
                    "Os campos obrigatórios variam de acordo com o tipo conta (Vendedor/comprador/motorista), mas os campos obrigatórios " +
                    "para qualquer tipo de cadastro são os seguintes : \n" +
                    "   * ***email***\n" +
                    "   * ***firstName***\n" +
                    "   * ***lastName***\n" +
                    "   * ***perfil***: *motorista/vendedor/comprador*\n\n" +
                    "**Obs :** *Para cada tipo de perfil, terá alguns campos do formulário que se tornarão obrigatórios.*\n\n" +
                    "Ao realizar o cadastro com sucesso, o usuário receberá um e-mail de boas vindas contendo as credenciais provisórias que são: " +
                    "o email cadastrado e uma senha aleatória que precisa ser alterada no primeiro acesso (*chamando o endpoint AlterPassword*)"
    )
    @ResponseStatus(HttpStatus.CREATED)
    CategoriaDTO create(CategoriaDTO dto);
    
	@ApiOperation(value = "SuperCategory", notes = "")
	@ResponseStatus(HttpStatus.CREATED)
	SuperCategoryDTO create(SuperCategoryDTO dto);
	
	@ApiOperation(value = "PromocaoFrete", notes = "")
	@ResponseStatus(HttpStatus.CREATED)
	PromocaoFreteDTO create(PromocaoFreteDTO dto);
}
