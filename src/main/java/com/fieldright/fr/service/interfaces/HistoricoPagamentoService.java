package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.HistoricoPagamento;
import com.fieldright.fr.entity.dto.HistoricoPagMotoristaDTO;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.util.enums.StatusHistoricoPagamento;
import com.fieldright.fr.util.enums.TipoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Optional;

public interface HistoricoPagamentoService {

    void saveOrUpdate(Conta conta,boolean isAutorizado,String transferCode);
    void save(Conta conta,String transferCode,boolean isAutorizado);
    void update(boolean isAutorizado,String transferCode);
    Optional<HistoricoPagamento> findByTransfereCode(String transferCode);
    Page<HistoricoPagamento> findByStatus(StatusHistoricoPagamento status,Conta conta);
    Response findPagamentoByUsuario(HistoricoPagMotoristaDTO dto, TipoUsuario tipoUsuario, Pageable pageable);

    Response findPagamentoByUsuario(Long id, TipoUsuario tipoUsuario, Timestamp dataInicio,Timestamp dataTermino, Pageable pageable);


    void save(HistoricoPagamento historicoPagamento)throws Exception;
}
