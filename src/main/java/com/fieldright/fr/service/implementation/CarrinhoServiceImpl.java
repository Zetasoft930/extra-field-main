package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.repository.CarrinhoRepository;
import com.fieldright.fr.service.interfaces.CarrinhoService;
import com.fieldright.fr.util.enums.StatusCompra;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CarrinhoServiceImpl implements CarrinhoService {

    private CarrinhoRepository carrinhoRepository;

    @Override
    public Carrinho internalSave(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    @Override
    public Carrinho internalFindById(long id) {
        return carrinhoRepository.findById(id).get();
    }

    @Override
    public void internalConfirmeCarrinho(long id) {
        /**
         * Se confirma o carrinho somente se nenhuma compra estiver copm status AGUARDANDO_PAGAMENTO
         */

        Carrinho carrinho = carrinhoRepository.findById(id).get();
        for (Compra compra : carrinho.getCompras()) {
            if (compra.getStatus().equals(StatusCompra.AGUARDANDO_PAGAMENTO)) {
                return;
            }
        }

        carrinho.setStatusCompra(StatusCompra.EM_PREPARACAO);
        carrinhoRepository.save(carrinho);
    }

    @Override
    public List<Carrinho> findAllAguardandoPagamento() {
        return carrinhoRepository.findAllCarrinhoByStatusCompra(StatusCompra.AGUARDANDO_PAGAMENTO);
    }

    @Override
    public void internalCanceleCarrinho(Carrinho carrinho) {
        carrinho.setStatusCompra(StatusCompra.CANCELADA);
        carrinhoRepository.save(carrinho);
    }

    @Override public void internalAddCodigoPagamento(long carrinhoId, String codigoPagamento) {
        Carrinho carrinho = this.internalFindById(carrinhoId);
        carrinho.setCodigoPagamento(codigoPagamento);
        this.internalSave(carrinho);
    }
}
