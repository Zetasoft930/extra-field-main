package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.*;
import com.fieldright.fr.entity.dto.ContaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.response.Response;

import java.util.List;

public interface ContaService {
    void internalCreateForNewVendedor(Vendedor vendedor);

    void internalCreateForNewVendedor(Vendedor vendedor, ContaDTO dto);

    void internalCreateForNewMotorista(Motorista motorista);

    void internalAcrescentaSaldoVendedor(Venda venda);

    Response<ContaDTO> updateAccount(ContaDTO conta, UserAuthenticated authenticated);

    void internalAcrescentaSaldoMotorista(Entrega entrega);

   List<Conta> findMotoristaNotInHistoricoPagmento();
}
