package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.WebhooksReceiverController;
import com.fieldright.fr.entity.emis.TransactionGPO;
import com.fieldright.fr.entity.pagSeguro.TransferInfos;
import com.fieldright.fr.service.interfaces.AdminService;
import com.fieldright.fr.service.interfaces.CompraService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@AllArgsConstructor
@RequestMapping("/hooks/catch")
@CrossOrigin
public class WebhooksReceiverControllerImpl implements WebhooksReceiverController {

    private AdminService adminService;
    private CompraService compraService;

    @PostMapping(
            value = "/pagSeguro"
    )
    @Override
    public void updateTransferInfosPagSeguro(@RequestBody TransferInfos transferInfos) {
        adminService.updateTransferInfosPagSeguro(transferInfos);
    }

    @PostMapping(
            value = "/notificateTransaction",
            params = {"notificationCode", "notificationType"}
    )
    @Override
    public void notificacaoPagamentoPagSeguro(@RequestParam String notificationCode, @RequestParam String notificationType) {
        compraService.pagamentoAtualizadoPagSeguro(notificationCode);
    }

    @Override
    @PostMapping(
            value = "/gpo"
    )
    public void pagamentoAtualizadoGPO(@RequestBody TransactionGPO transactionGPO) {
        compraService.pagamentoAtualizadoEMIS(transactionGPO);
    }
}
