package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.HistoricoPagamento;
import com.fieldright.fr.entity.dto.HistoricoCompraDTO;
import com.fieldright.fr.entity.dto.HistoricoPagMotoristaDTO;
import com.fieldright.fr.repository.HistoricoPagamentoRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.HistoricoPagamentoService;
import com.fieldright.fr.util.enums.StatusHistoricoPagamento;
import com.fieldright.fr.util.enums.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class HistoricoPagamentoServiceImpl implements HistoricoPagamentoService {


    @Autowired
    private HistoricoPagamentoRepository repository;


    @Override
    public void saveOrUpdate(Conta conta, boolean isAutorizado,String transferCode) {


        try
        {

            StatusHistoricoPagamento status = isAutorizado ? StatusHistoricoPagamento.RECEBIDO : StatusHistoricoPagamento.PENDENTE;

            Optional<HistoricoPagamento> optional = this.repository.findByStatusAndTransfereCode(transferCode);

            if(optional.isPresent())
            {
                HistoricoPagamento model = optional.get();
                model.setStatus(status);
                repository.save(model);

            }else{

                this.save(conta,transferCode,isAutorizado);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    @Override
    public void save(Conta conta,String transferCode,boolean isAutorizado) {

        if(conta.getSaldo().doubleValue() > 0) {

            StatusHistoricoPagamento status = isAutorizado ? StatusHistoricoPagamento.RECEBIDO : StatusHistoricoPagamento.PENDENTE;

            HistoricoPagamento model = new HistoricoPagamento();
            model.setStatus(status);
            model.setTransferCode(transferCode);
            model.setConta(conta);
            model.setValor(conta.getSaldo());
            repository.save(model);
        }

    }

    @Override
    public void update(boolean isAutorizado,String transferCode) {

        StatusHistoricoPagamento status = isAutorizado ? StatusHistoricoPagamento.RECEBIDO : StatusHistoricoPagamento.PENDENTE;

       Optional<HistoricoPagamento> optional = this.repository.findByStatusAndTransfereCode(transferCode);

       if(optional.isPresent())
       {
           HistoricoPagamento model = optional.get();
           model.setStatus(status);
           repository.save(model);
       }

    }

    @Override
    public Optional<HistoricoPagamento> findByTransfereCode(String transferCode) {
        return Optional.empty();
    }

    @Override
    public Page<HistoricoPagamento> findByStatus(StatusHistoricoPagamento status, Conta conta) {
        return null;
    }

    @Override
    public Response<Page<HistoricoCompraDTO>> findPagamentoByUsuario(HistoricoPagMotoristaDTO dto, TipoUsuario tipoUsuario, Pageable pageable) {

        Page<HistoricoCompraDTO> dtoPage = null;
        Page<Object[]> pages = repository.findPagamentoByUsuario(dto.getUserId(),tipoUsuario.getText(),dto.getDataInicio(),dto.getDataTermino(),pageable);



        dtoPage =   pages.map( p ->{

              return  HistoricoCompraDTO.builder()
                      .montante(BigDecimal.valueOf(Double.parseDouble(String.valueOf(p[0]))))
                      .data(String.valueOf(p[1]))
                      .status(String.valueOf(p[2]))
                      .build();

        });

        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtoPage)
                .withErrors(null)
                .build();

    }

    @Override
    public Response findPagamentoByUsuario(Long id, TipoUsuario tipoUsuario, Timestamp dataInicio, Timestamp dataTermino, Pageable pageable) {

        Page<HistoricoCompraDTO> dtoPage = null;
        Page<Object[]> pages = repository.findPagamentoByUsuario(id,tipoUsuario.getText(),dataInicio,dataTermino,pageable);



        dtoPage =   pages.map( p ->{

            return  HistoricoCompraDTO.builder()
                    .montante(BigDecimal.valueOf(Double.parseDouble(String.valueOf(p[0]))))
                    .data(String.valueOf(p[1]))
                    .status(String.valueOf(p[2]))
                    .build();

        });

        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtoPage)
                .withErrors(null)
                .build();

    }

    @Override
    public void save(HistoricoPagamento model) throws Exception{

        try
        {
            repository.save(model);
        }catch (Exception e)
        {
            throw e;
        }

    }


}
