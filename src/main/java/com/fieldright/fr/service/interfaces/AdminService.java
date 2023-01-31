package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.PromocaoFrete;
import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.entity.dto.*;
import com.fieldright.fr.entity.pagSeguro.TransferInfos;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AdminService {
    Response<List<UserDTO>> ativeUsuario(long userId, UserAuthenticated authenticated);

    Response<List<UserDTO>> desativeUsuario(long userId, UserAuthenticated authenticated);

    Response<List<Category>> addCategory(String categoryName, UserAuthenticated authenticated);

    Response<List<Category>> deleteCategory(String categoryName, UserAuthenticated authenticated);

    Response<List<MoneyConfig>> findAllMoneyConfig(UserAuthenticated authenticated);

    Response<MoneyConfig> editMoneyConfig(long id, double valorMínimo, UserAuthenticated authenticated);

    Response<List<UserDTO>> findAllUser(UserAuthenticated authenticated);

    Response<List<ProductDTO>> findAllProduct(UserAuthenticated authenticated);

    Response<List<VendaDTO>> findAllVenda(UserAuthenticated authenticated);

    Response<List<CompraDTO>> findAllCompra(UserAuthenticated authenticated);

    Response<List<EntregaDTO>> findAllEntrega(UserAuthenticated authenticated);

    Response<Taxa> editTaxa(long id, double percentual, UserAuthenticated authenticated);

    Response<List<RemuneracaoEntregadoresConfig>> findAllMoneyConfigDelivery(UserAuthenticated authenticated);

    Response<RemuneracaoEntregadoresConfig> editMoneyConfigDelivery(RemuneracaoEntregadoresConfig config, UserAuthenticated authenticated);

    Response<HttpStatus> pay(UserAuthenticated authenticated, long userId);

    Response<List<UserDTO>> createVendedor(UserAuthenticated authenticated, UserDTO vendedor);

    Response<List<ProductDTO>> deleteProduct(UserAuthenticated authenticated, long productId);

    Response<List<Avaliacao>> findAllAvaliacao(UserAuthenticated authenticated);

    Response<List<CompraDTO>> alterStatusCompra(long compraId, String newStatus, UserAuthenticated authenticated);

    void updateTransferInfosPagSeguro(TransferInfos transferInfos);

    void canceleComprasNaoPagas(UserAuthenticated authenticated);
    
    Response aprovarDepoimento(long depoimentoId, UserAuthenticated authenticated);
    
    SuperCategoryDTO create(SuperCategoryDTO superCategoria,  UserAuthenticated authenticated);
    
    CategoriaDTO create(CategoriaDTO category, UserAuthenticated authenticated);
    
    PromocaoFreteDTO save(PromocaoFreteDTO frete,  UserAuthenticated authenticated);
}
