package com.fieldright.fr.controller.interfaces;

import com.fieldright.fr.entity.emis.TransactionGPO;
import com.fieldright.fr.entity.pagSeguro.TransferInfos;
import io.swagger.annotations.Api;

@Api(
        value = "Webhook notifications",
        description = "Endpoints para receber dados dos webhooks"
)
public interface WebhooksReceiverController {

    void updateTransferInfosPagSeguro(TransferInfos transferInfos);

    void notificacaoPagamentoPagSeguro(String notificationCode, String notificationType);

    void pagamentoAtualizadoGPO(TransactionGPO transactionGPO);
}
