package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.response.XmlBody;
import com.fieldright.fr.service.implementation.PagSeguroServiceImpl;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;

public interface PagSeguroService {

    double consultaSaldo();

    String solicitaAutorizacao(String email, BigDecimal valor);

    PagSeguroServiceImpl.JsonResponse autorizaPagamento(String authorizationCode);

    String efetuaPagamentoDeProdutos(XmlBody xmlBody) throws IOException, JAXBException;

    PagSeguroServiceImpl.XmlTransactionResponse consultaPagamento(String notificationCode);
}
