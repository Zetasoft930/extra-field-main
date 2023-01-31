package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.repository.VendaRepository;
import com.fieldright.fr.service.interfaces.CompraService;
import com.fieldright.fr.service.interfaces.EntregaService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.exception.PermissionDeniedException;
import com.fieldright.fr.util.mapper.VendaMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VendaServiceImplTest {

    @Mock
    private VendaServiceImpl service;
    @Mock
    private UserService userService;
    @Mock
    private EntregaService entregaService;
    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private CompraService compraService;
    @Mock
    private VendaMapper vendaMapper;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        service = new VendaServiceImpl(userService, compraService, vendaRepository, vendaMapper);
        service.setEntregaService(entregaService);
    }

    @Test
    public void deveGerarEntregaCasoLojaNaoPossuirEntregadoresProprios() {
        UserAuthenticated authenticated = geraUsuarioLogado();
        Venda venda = geraVendaEmPreparacao();
        Vendedor vendedor = geraVendedorComVenda(venda, false);
        Optional<Venda> vendas = Optional.of(venda);

        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);
        when(vendaRepository.findById(anyLong())).thenReturn(vendas);
        when(vendaMapper.toVendaDTO(any(Venda.class))).thenReturn(new VendaDTO());

        service.ready(1, authenticated);

        verify(entregaService, times(1)).internalGereNovaEntrega(any(Venda.class));
    }

    @Test
    public void naoDeveGerarEntregaCasoLojaPossuirSeusEntregadores() {
        UserAuthenticated authenticated = geraUsuarioLogado();
        Venda venda = geraVendaEmPreparacao();
        Vendedor vendedor = geraVendedorComVenda(venda, true);
        Optional<Venda> vendas = Optional.of(venda);

        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);
        when(vendaRepository.findById(anyLong())).thenReturn(vendas);

        service.ready(1, authenticated);

        verify(entregaService, times(0)).internalGereNovaEntrega(any(Venda.class));
    }

    @Test
    public void deveMarcarPedidoACaminhoCasoLojaPossuirEntregadoresProprio(){
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(true);

        setUpMocks(vendedor);

        UserAuthenticated authenticated = new UserAuthenticated(1, "email@email.com");
        service.pedidoACaminho(1L, authenticated);

        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    public void naoDeveMarcarPedidoACaminhoCasoLojaNaoPossuirEntregadoresProprio(){
        exception.expect(PermissionDeniedException.class);
        exception.expectMessage("Não foi possível realizar esta ação: Esta loja não possui entregadores próprios");

        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(false);

        when(userService.internalFindUserById(1L)).thenReturn(vendedor);

        UserAuthenticated authenticated = new UserAuthenticated(1, "email@email.com");
        service.pedidoACaminho(1L, authenticated);
    }

    @Test
    public void deveMarcarPedidoFinalizadoCasoLojaPossuirEntregadoresProprio(){
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(true);

        setUpMocks(vendedor);

        UserAuthenticated authenticated = new UserAuthenticated(1, "email@email.com");
        service.pedidoFinalizado(1L, authenticated);

        verify(vendaRepository, times(1)).save(any(Venda.class));
    }

    @Test
    public void naoDeveMarcarPedidoFinalizadoCasoLojaNaoPossuirEntregadoresProprio(){
        exception.expect(PermissionDeniedException.class);
        exception.expectMessage("Não foi possível realizar esta ação: Esta loja não possui entregadores próprios");

        Vendedor vendedor = new Vendedor();

        when(userService.internalFindUserById(1L)).thenReturn(vendedor);

        UserAuthenticated authenticated = new UserAuthenticated(1, "email@email.com");
        service.pedidoFinalizado(1L, authenticated);
    }

    private Vendedor geraVendedorComVenda(Venda venda, boolean possuiEntregadores) {
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(possuiEntregadores);
        vendedor.setVendas(Collections.singletonList(venda));
        return vendedor;
    }

    private Venda geraVendaEmPreparacao() {
        Venda venda = new Venda();
        venda.setStatus(StatusVenda.EM_PREPARACAO);
        return venda;
    }

    private UserAuthenticated geraUsuarioLogado() {
        UserAuthenticated authenticated = new UserAuthenticated();
        authenticated.setId(3);
        return authenticated;
    }

    private void setUpMocks(Vendedor vendedor) {
        Venda venda = new Venda();
        venda.setCompras(Collections.emptyList());
        Optional<Venda> optionalVenda = Optional.of(venda);

        when(userService.internalFindUserById(1L)).thenReturn(vendedor);
        when(vendaRepository.findById(1L)).thenReturn(optionalVenda);
        doNothing().when(compraService).internalComprasACaminho(anyListOf(Compra.class));
        when(vendaRepository.save(any(Venda.class))).thenReturn(venda);
    }

}
