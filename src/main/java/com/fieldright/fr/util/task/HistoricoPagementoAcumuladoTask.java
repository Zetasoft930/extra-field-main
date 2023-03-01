package com.fieldright.fr.util.task;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.HistoricoPagamento;
import com.fieldright.fr.repository.DataBaseConnector;
import com.fieldright.fr.service.interfaces.ContaService;
import com.fieldright.fr.service.interfaces.HistoricoPagamentoService;
import com.fieldright.fr.util.enums.StatusHistoricoPagamento;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.ForeignKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Essa task é responsável para desativar todos os vendedores com data de desativação menor do que a data atual.
 * Ela será executada todos os dias as 00h00, hora do servidor.
 */
@Component
public class HistoricoPagementoAcumuladoTask  {

    private static final Logger log = LoggerFactory.getLogger(HistoricoPagementoAcumuladoTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private HistoricoPagamentoService historicoPagamentoService;
    @Autowired
    private ContaService contaService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {

        if(agendamentoTask()) {
            log.info("<<<< is Task start now {} >>>>", dateFormat.format(new Date()));
            for (Conta conta : contaService.findMotoristaNotInHistoricoPagmento()) {


                this.saveHistoricoDataAnterior(conta);


            }
            log.info("<<<< end task {} >>>>", dateFormat.format(new Date()));
        }
    }

    private boolean agendamentoTask(){

        return  false;

    }

    private HistoricoPagamento preecherDadoHistorico(Conta conta)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        HistoricoPagamento historicoPagamento = new HistoricoPagamento();
        historicoPagamento.setValor(conta.getSaldo());

        historicoPagamento.setConta(conta);
        historicoPagamento.setTransferCode("ACUMULADO-"+conta.getCPFouCNPJ()+simpleDateFormat.format(new Date()));
        historicoPagamento.setStatus(StatusHistoricoPagamento.ACUMULADO);


        return historicoPagamento;
    }
    private void saveHistoricoDataAnterior(Conta conta)
    {

        try
        {

            System.out.println("<<<<< TASK saveHistoricoDataAnterior >>>>>");
            HistoricoPagamento historicoPagamento = preecherDadoHistorico(conta);
            this.historicoPagamentoService.save(historicoPagamento);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

}
