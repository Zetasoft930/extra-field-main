package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.dto.*;
import com.fieldright.fr.entity.emis.TransactionGPO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;
import org.springframework.http.HttpStatus;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface CompraService {

    Response<String> newCompra(CarrinhoDTO carrinho, UserAuthenticated authenticated, String pais) throws IOException, JAXBException;

    void pagamentoAtualizadoPagSeguro(String notificationCode);

    Response<CompraDTO> findById(long id);

    void internalCanceleCompra(Compra compra);

    void internalConfirmeCompras(List<Compra> compras);

    void internalCanceleCompras(List<Compra> compras);

    void internalComprasACaminho(List<Compra> compras);

    void internalComprasFinalizadas(List<Compra> compras);

    Response<HttpStatus> evaluate(Avaliacao avaliacao, long lojaId, UserAuthenticated authenticated);

    Response<List<CarrinhoDTO>> findAll(UserAuthenticated authenticated);

    void canceleComprasNaoPagas();

    Response<BigDecimal> calculeFrete(CarrinhoDTO carrinho);

    void pagamentoAtualizadoEMIS(TransactionGPO transactionGPO);
    
    void sendPush(UserAuthenticated authenticated, String mensagem);
    
    Response<BigDecimal> price(CompraDTO dto);

    Response<PrecoDTO> newprice(ProductPriceDTO productPriceDTO);
    public BigDecimal getQtCompraCalc(String unidadeProduto,String unidadeCompra,int qtdComprada);
}
