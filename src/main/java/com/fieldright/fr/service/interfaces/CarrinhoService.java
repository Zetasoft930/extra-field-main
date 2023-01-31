package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Carrinho;

import java.util.List;

public interface CarrinhoService {
    Carrinho internalSave(Carrinho carrinho);

    Carrinho internalFindById(long id);

    void internalConfirmeCarrinho(long id);

    List<Carrinho> findAllAguardandoPagamento();

    void internalCanceleCarrinho(Carrinho carrinho);

    void internalAddCodigoPagamento(long carrinhoId, String codigoPagamento);
}
