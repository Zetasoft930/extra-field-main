package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.*;
import com.fieldright.fr.entity.dto.ContaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.repository.ContaRepository;
import com.fieldright.fr.repository.TaxaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ContaService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.exception.ContaNotFoundException;
import com.fieldright.fr.util.mapper.ContaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class ContaServiceImpl implements ContaService {

    private static final String ERRO_CONTA_NOT_FOUND_BY_USERID_MESSAGE = "Não foi encontrado nenhuma conta para o " +
            "usuário informado. UserId: ";
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private TaxaRepository taxaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ContaMapper contaMapper;

    public ContaServiceImpl(UserService userService, ContaRepository contaRepository, TaxaRepository taxaRepository) {
        this.userService = userService;
        this.contaRepository = contaRepository;
        this.taxaRepository = taxaRepository;
    }

    @Override
    public void internalCreateForNewVendedor(Vendedor vendedor) {
        Conta conta = new Conta();
        conta.setUsuarioId(vendedor.getId());
        conta.setNome(vendedor.getFullName());
        conta.setCPFouCNPJ(vendedor.getCPFouCNPJ());
        conta.setSaldo(new BigDecimal(0));
        vendedor.setConta(contaRepository.save(conta));
        userService.internalUpdateUser(vendedor);
    }

    @Override
    public void internalCreateForNewVendedor(Vendedor vendedor, ContaDTO dto) {
        Conta conta = contaMapper.toConta(dto);
        conta.setUsuarioId(vendedor.getId());
        conta.setSaldo(new BigDecimal(0));
        vendedor.setConta(contaRepository.save(conta));
        userService.internalUpdateUser(vendedor);
    }

    @Override
    public void internalCreateForNewMotorista(Motorista motorista) {
        Conta conta = new Conta();
        conta.setUsuarioId(motorista.getId());
        conta.setNome(motorista.getFullName());
        conta.setCPFouCNPJ(motorista.getCpf());
        conta.setSaldo(new BigDecimal(0));
        motorista.setConta(contaRepository.save(conta));
        userService.internalUpdateUser(motorista);
    }

    @Override
    public void internalAcrescentaSaldoVendedor(Venda venda) {
        Optional<Conta> optionalConta = getConta(venda.getVendedorId());

        BigDecimal vl = calculeValorAReceberDoVendedor(venda);
        Conta conta = optionalConta.get();
        conta.setSaldo(conta.getSaldo().add(vl));
        contaRepository.save(conta);
    }

    @NotNull
    private Optional<Conta> getConta(long usuarioID) {
        Optional<Conta> optionalConta = contaRepository.findByUsuarioId(usuarioID);

        if (!optionalConta.isPresent())
            throw new ContaNotFoundException(ERRO_CONTA_NOT_FOUND_BY_USERID_MESSAGE + usuarioID);
        return optionalConta;
    }

    @Override
    public Response<ContaDTO> updateAccount(ContaDTO dto, UserAuthenticated authenticated) {
        Optional<Conta> optionalConta = getConta(authenticated.getId());

        Conta account = optionalConta.get();
        updateAccountInformations(dto, account);

        return returnAccountDTOInResponse(HttpStatus.OK, contaRepository.save(account), null);
    }

    @Override
    public void internalAcrescentaSaldoMotorista(Entrega entrega) {
        Optional<Conta> optionalConta = getConta(entrega.getEntregadorId());
        Conta conta = optionalConta.get();
        conta.setSaldo(conta.getSaldo().add(entrega.getValorRemuneracao()));
        contaRepository.save(conta);
    }

    private Response<ContaDTO> returnAccountDTOInResponse(HttpStatus status, Conta conta, ArrayList<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(contaMapper.toContaDTO(conta))
                .withErrors(errors)
                .build();
    }

    private void updateAccountInformations(ContaDTO dto, Conta account) {
        String emailPagSeguro = (dto.getEmailPagSeguro() == null) ? account.getEmailPagSeguro() : dto.getEmailPagSeguro();
        String chavePix = (dto.getChavePix() == null) ? account.getChavePix() : dto.getChavePix();
        String nome = (dto.getNome() == null) ? account.getNome() : dto.getNome();
        String instituicao = (dto.getInstituicao() == null) ? account.getInstituicao() : dto.getInstituicao();
        String agencia = (dto.getAgencia() == null) ? account.getAgencia() : dto.getAgencia();
        String nuConta = (dto.getNuConta() == null) ? account.getNuConta() : dto.getNuConta();
        String CPFouCNPJ = (dto.getCPFouCNPJ() == null) ? account.getCPFouCNPJ() : dto.getCPFouCNPJ();
        Character tipoConta = dto.getTipoConta();
        Character digito = dto.getDigito();
        Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());

        account.setEmailPagSeguro(emailPagSeguro);
        account.setChavePix(chavePix);
        account.setNome(nome);
        account.setInstituicao(instituicao);
        account.setAgencia(agencia);
        account.setNuConta(nuConta);
        account.setCPFouCNPJ(CPFouCNPJ);
        account.setTipoConta(tipoConta);
        account.setDigito(digito);
        account.setModifiedAt(modifiedAt);
    }

    private BigDecimal calculeValorAReceberDoVendedor(Venda venda) {
        BigDecimal valorVenda = venda.getVlTotal();
        double fieldRigthPercent = taxaRepository.findById(1L).get().getPercentual();
        double deliveryPercent = taxaRepository.findById(2L).get().getPercentual();
        double peASubtrair = fieldRigthPercent;

        if (!vendedorPossuiEntregadoresProprios(venda.getVendedorId())) {
            peASubtrair += deliveryPercent;
        }

        BigDecimal vlTaxa = valorVenda.multiply(new BigDecimal(peASubtrair)).divide(new BigDecimal(100));

        return venda.getVlTotal().subtract(vlTaxa);
    }

    private boolean vendedorPossuiEntregadoresProprios(Long vendedorId) {
        Vendedor vendedor = (Vendedor) userService.internalFindUserById(vendedorId);
        return vendedor.getPossuiEntregadores();
    }
}
