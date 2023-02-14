package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.mail.EMailSender;
import com.fieldright.fr.notification.PushSender;
import com.fieldright.fr.repository.VendaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.*;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.exception.PermissionDeniedException;
import com.fieldright.fr.util.mapper.VendaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class VendaServiceImpl implements VendaService {

    private static final String ERRO_AO_CONFIRMAR_VENDA_MESSAGE = "Não foi possível confirmar a venda informada";
    private static final String ERRO_AO_CANCELAR_VENDA_MESSAGE = "Não foi possível cancelar a venda informada";
    @Autowired
    private PushSender pushSender;
    @Autowired
    private EMailSender EMailSender;
    @Autowired
    private VendaMapper vendaMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ContaService contaService;
    @Autowired
    private CompraService compraService;
    @Autowired
    private EntregaService entregaService;
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private VendaRepository vendaRepository;

    public VendaServiceImpl(UserService userService, CompraService compraService, VendaRepository vendaRepository, VendaMapper vendaMapper) {
        this.userService = userService;
        this.compraService = compraService;
        this.vendaRepository = vendaRepository;
        this.vendaMapper = vendaMapper;
    }

    public void setEntregaService(EntregaService entregaService){
        this.entregaService = entregaService;
    }

    /**
     * * Só vendedores podem confirmar vendas
     * * A venda informada deve pertencer ao vendedor logado para ele poder confirmar
     * * Só se pode confirmar umna venda com status NOVA
     * * Atualizar o status da venda das compras e do carrinho (caso todas as compras foram confirmadas)
     * * Atualizar o saldo do vendedor
     * * Enviar a notificação de confirmação para o comprador
     *
     * @param id
     * @param authenticated
     * @return
     */
    @Override
    public Response<HttpStatus> confirm(long id, UserAuthenticated authenticated) {
        try {
            Vendedor vendedor = (Vendedor) userService.internalFindUserById(authenticated.getId());
            Venda venda = vendaRepository.findById(id).get();
            if (venda.getStatus().equals(StatusVenda.NOVA) && vendedor.getVendas().contains(venda)) {
                venda.setStatus(StatusVenda.EM_PREPARACAO);
                venda.setConfirmadaAt(new Timestamp(System.currentTimeMillis()));
                compraService.internalConfirmeCompras(venda.getCompras());
                carrinhoService.internalConfirmeCarrinho(venda.getCarrinhoId());
                vendaRepository.save(venda);
                contaService.internalAcrescentaSaldoVendedor(venda);
                pushSender.avisaCompraConfirmada(venda.getCompradorId());
                return returnHttpStatusInResponse(HttpStatus.OK, HttpStatus.OK, null);
            }
            return returnHttpStatusInResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(ERRO_AO_CONFIRMAR_VENDA_MESSAGE));
        } catch (RuntimeException e) {
            return returnHttpStatusInResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(e.getMessage()));
        }
    }

    private Response<HttpStatus> returnHttpStatusInResponse(HttpStatus status, HttpStatus httpStatus, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(httpStatus)
                .withErrors(errors)
                .build();
    }

    /**
     * Só vendedores podem cancelar vendas
     * A venda informada deve pertencer ao vendedor logado para ele poder cancelar
     * Só se pode cancelar umna venda com status NOVA
     * Cancelar todas as compras desta venda
     * Atualizar o estoque de todos os produtos comprados
     * Atualizar o status da venda
     * Enviar a notificação de cancelamento para o comprador
     */
    @Override
    public Response<HttpStatus> cancel(long id, UserAuthenticated authenticated) {
        Vendedor vendedor = (Vendedor) userService.internalFindUserById(authenticated.getId());
        Venda venda = vendaRepository.findById(id).get();
        if (venda.getStatus().equals(StatusVenda.NOVA) && vendedor.getVendas().contains(venda)) {
            venda.setStatus(StatusVenda.CANCELADA);
            venda.setRejeitadaAt(new Timestamp(System.currentTimeMillis()));
            compraService.internalCanceleCompras(venda.getCompras());
            vendaRepository.save(venda);
            pushSender.avisaCompraCancelada(venda.getCompradorId());
            return returnHttpStatusInResponse(HttpStatus.OK, HttpStatus.OK, null);
        }
        return returnHttpStatusInResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList(ERRO_AO_CANCELAR_VENDA_MESSAGE));
    }

    /**
     * Este método é para confirmar a preparação do produto.
     * * Só vendedores podem fazer esta ação
     * * A venda informada deve pertencer ao vendedor logado
     * * Só se pode confirmar uma venda com status "Em_PREPARAÇÂO"
     * * Alterar o status da venda para "AGUARDANDO_ENTREGADOR"
     * * Gerar uma nova entrega
     *
     * @param id
     * @param authenticated
     * @return
     */
    @Override
    public Response<VendaDTO> ready(long id, UserAuthenticated authenticated) {
        Vendedor vendedor = (Vendedor) userService.internalFindUserById(authenticated.getId());
        Venda venda = vendaRepository.findById(id).get();
        if (venda.getStatus().equals(StatusVenda.EM_PREPARACAO) && vendedor.getVendas().contains(venda)) {
            venda.setStatus(StatusVenda.AGUARDANDO_ENTREGADOR);
            gereNovaEntregaSeForPreciso(vendedor, venda);
            vendaRepository.save(venda);
            return returnVendaDTOInResponse(HttpStatus.OK, vendaMapper.toVendaDTO(venda), null);
        }
        return returnVendaDTOInResponse(HttpStatus.BAD_REQUEST, null, Arrays.asList("Não foi possível realizar a ação solicitada"));
    }

    private void gereNovaEntregaSeForPreciso(Vendedor vendedor, Venda venda) {
        if (!vendedor.getPossuiEntregadores()) {
            entregaService.internalGereNovaEntrega(venda);
        }
    }

    /**
     * Alterar o status da venda
     * Alterar o status de todas as compras
     *
     * @param vendaId
     */
    @Override
    public void internalVendaACaminho(long vendaId) {
        Venda venda = vendaRepository.findById(vendaId).get();
        compraService.internalComprasACaminho(venda.getCompras());
        venda.setStatus(StatusVenda.A_CAMINHO);
        venda.setACaminhoAt(new Timestamp(System.currentTimeMillis()));
        vendaRepository.save(venda);
    }

    /**
     * Alterar o status da venda
     * Alterar o status de todas as compras
     *
     * @param vendaId
     */
    @Override
    public void internalVendaFinalizada(long vendaId) {
        Venda venda = vendaRepository.findById(vendaId).get();
        compraService.internalComprasFinalizadas(venda.getCompras());
        venda.setStatus(StatusVenda.FINALIZADA);
        venda.setFinalizadaAt(new Timestamp(System.currentTimeMillis()));
        vendaRepository.save(venda);
    }

    @Override
    public Response<List<VendaDTO>> findAll(UserAuthenticated authenticated) {
        Vendedor vendedor = (Vendedor) userService.internalFindUserById(authenticated.getId());
        return returnVendaDTOListInResponse(HttpStatus.OK, vendaMapper.toVendaDTOs(vendedor.getVendas()), null);
    }

    @Override
    public void pedidoACaminho(Long vendaId, UserAuthenticated authenticated) throws PermissionDeniedException{
        validaLojaComEntregadoresProprio(authenticated);
        internalVendaACaminho(vendaId);
    }

    @Override
    public void pedidoFinalizado(Long vendaId, UserAuthenticated authenticated) throws PermissionDeniedException {
        validaLojaComEntregadoresProprio(authenticated);
        internalVendaFinalizada(vendaId);
    }

    //NOVO CODIGO
    @Override
    public Response<BigDecimal> countVendaByVendedorAndStatus(long usuarioLojaId, StatusVenda status) {

        Optional<BigDecimal> totalVendaOptional =  vendaRepository.countVendaByVendedorAndStatus(usuarioLojaId,status.getText());
        BigDecimal totalVenda = BigDecimal.ZERO;

        if(totalVendaOptional.isPresent())
        {
            totalVenda = totalVendaOptional.get();
        }

        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(totalVenda)
                .withErrors(null)
                .build();
    }

    private void validaLojaComEntregadoresProprio(UserAuthenticated authenticated) {
        Vendedor vendedor = (Vendedor) userService.internalFindUserById(authenticated.getId());
        if (vendedor.getPossuiEntregadores() == null || !vendedor.getPossuiEntregadores()) {
            throw new PermissionDeniedException("Não foi possível realizar esta ação: Esta loja não possui entregadores próprios");
        }
    }

    private Response<VendaDTO> returnVendaDTOInResponse(HttpStatus status, VendaDTO dto, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(dto)
                .withErrors(errors)
                .build();
    }

    private Response<List<VendaDTO>> returnVendaDTOListInResponse(HttpStatus status, List<VendaDTO> vendaDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(vendaDTOS)
                .withErrors(errors)
                .build();
    }
}
