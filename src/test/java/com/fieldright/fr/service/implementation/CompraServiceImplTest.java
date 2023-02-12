package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.TabelaFrete;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.entity.dto.CarrinhoDTO;
import com.fieldright.fr.entity.dto.CompraDTO;
import com.fieldright.fr.entity.emis.TransactionGPO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.mail.EMailSender;
import com.fieldright.fr.repository.CompraRepository;
import com.fieldright.fr.repository.MoneyConfigRepository;
import com.fieldright.fr.repository.TabelaFreteRepository;
import com.fieldright.fr.repository.TaxaRepository;
import com.fieldright.fr.repository.VendaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.response.XmlBody;
import com.fieldright.fr.service.interfaces.CarrinhoService;
import com.fieldright.fr.service.interfaces.CompraService;
import com.fieldright.fr.service.interfaces.EmisService;
import com.fieldright.fr.service.interfaces.PagSeguroService;
import com.fieldright.fr.service.interfaces.ProductService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.StatusCompra;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompraServiceImplTest {

    private CompraService compraService;
    private EnderecoServiceImpl enderecoService;
    @Mock
    private CompraRepository compraRepository;
    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private TaxaRepository taxaRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private TabelaFreteRepository tabelaFreteRepository;
    @Mock
    private MoneyConfigRepository moneyConfigRepository;
    @Mock
    private PagSeguroService pagSeguroService;
    @Mock
    private EmisService emisService;
    @Mock
    private CarrinhoService carrinhoService;
    @Mock
    private EMailSender eMailSender;

    @Before
    public void setUp() {
        Mockito.clearInvocations(productService, userService, tabelaFreteRepository, moneyConfigRepository,
                pagSeguroService, compraRepository, carrinhoService, emisService, vendaRepository, taxaRepository);

        enderecoService = Mockito.mock(EnderecoServiceImpl.class);
        compraService = new CompraServiceImpl(compraRepository, productService, userService, tabelaFreteRepository, moneyConfigRepository,
                pagSeguroService, emisService, enderecoService, carrinhoService, eMailSender, vendaRepository, taxaRepository);
    }

    @Test
    public void deveRetornarValorDeFreteZeradoQuandoALojaTiverSeusPropriosEntregadores() {
        Endereco enderecoEntrega = geraEndereco("Curitiba");
        Endereco enderecoLoja = geraEndereco("Floripa");
        CarrinhoDTO carrinhoDTO = umCarrinhoDTOCom(umaCompraDTOCom(enderecoEntrega));
        Product product = umProdutoCom(1L, 25.0);
        Vendedor vendedor = umVendedorCom(enderecoLoja, true);
        geraTabelaFrete(50.0, 150.0, new BigDecimal(47));
        when(productService.internalFindById(anyLong())).thenReturn(product);
        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);

        Response<BigDecimal> frete = compraService.calculeFrete(carrinhoDTO);

        Assert.assertEquals(new BigDecimal(0).doubleValue(), frete.getData().doubleValue(), 0.0);
    }

    @Test
    public void deveRetornarValorDeFreteQuandoALojaOptarPorUsarEntregadoresDaFieldRight() {
        Endereco enderecoEntrega = geraEndereco("Curitiba");
        Endereco enderecoLoja = geraEndereco("Floripa");
        CarrinhoDTO carrinhoDTO = umCarrinhoDTOCom(umaCompraDTOCom(enderecoEntrega));
        Product product = umProdutoCom(1L, 25.0);
        Vendedor vendedor = umVendedorCom(enderecoLoja, false);
        geraTabelaFrete(50.0, 150.0, new BigDecimal(47));
        when(productService.internalFindById(anyLong())).thenReturn(product);
        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);

        Response<BigDecimal> frete = compraService.calculeFrete(carrinhoDTO);

        Assert.assertEquals(new BigDecimal(62).doubleValue(), frete.getData().doubleValue(), 0.0);
    }

    @Test
    public void deveRetornarValorDeFreteQuandoALojaNaoEscolherOTipoDeFrete() {
        Endereco enderecoEntrega = geraEndereco("Curitiba");
        Endereco enderecoLoja = geraEndereco("Floripa");
        CarrinhoDTO carrinhoDTO = umCarrinhoDTOCom(umaCompraDTOCom(enderecoEntrega));
        Product product = umProdutoCom(1L, 25.0);
        Vendedor vendedor = umVendedorCom(enderecoLoja, null);
        geraTabelaFrete(50.0, 150.0, new BigDecimal(47));
        when(productService.internalFindById(anyLong())).thenReturn(product);
        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);

        Response<BigDecimal> frete = compraService.calculeFrete(carrinhoDTO);

        Assert.assertEquals(new BigDecimal(62).doubleValue(), frete.getData().doubleValue(), 0.0);
    }

    @Test
    public void deveLancarExecaoCasoOPaisNaoEhSuportado() throws JAXBException, IOException {
        when(moneyConfigRepository.findByPaisCodigoLike("FRA")).thenReturn(Optional.empty());

        Response<String> response = compraService.newCompra(new CarrinhoDTO(), new UserAuthenticated(), "FRA");

        Assert.assertEquals("Compras não são permitidas para o país informado: FRA", response.getErrors().get(0));
    }

    @Test
    public void deveRetornarErroQuandoNaoTiverProdutoDisponivelParaCompra() throws JAXBException, IOException {
        moneyConfigMock("BRA", 50.0);
        usuarioByIdMock(1, new Comprador());
        usuarioByIdMock(2, new Vendedor());
        when(productService.internalFindById(Mockito.anyLong())).thenReturn(umProdutoCom(BigDecimal.ONE, "Banana", 2, 0));

        Response<String> response = compraService.newCompra(umCarrinhoDTOCom(umaCompraDTOCom(new Endereco(), BigDecimal.valueOf(100), 100)), umUsuarioAutenticadoCom(1L), "BRA");

        Assert.assertEquals(1, response.getErrors().size());
        Assert.assertEquals("Não foi possível realizar a compra pois não tem quantidade suficiente para sua compra do produto: Banana", response.getErrors().get(0));
    }

    @Test
    public void deveRetornarErroQuandoOValorPagoDivergirDoValorASerPago() throws JAXBException, IOException {
        moneyConfigMock("BRA", 50.0);
        usuarioByIdMock(1, new Comprador());
        usuarioByIdMock(2, new Vendedor());
        when(productService.internalFindById(Mockito.anyLong())).thenReturn(umProdutoCom(BigDecimal.ONE, "Banana", 2, 55));

        Response<String> response = compraService.newCompra(umCarrinhoDTOCom(umaCompraDTOCom(new Endereco(), BigDecimal.valueOf(100), 55)), umUsuarioAutenticadoCom(1L), "BRA");

        Assert.assertEquals(1, response.getErrors().size());
        Assert.assertEquals("Não foi possível realizar a compra pois o valor a ser pago diverge do valor que precisa ser pago no produto: Banana", response.getErrors().get(0));
    }

    @Test
    public void deveRetornarErroQuandoOValorDaCompraForMenorQueOMinimoPermitido() throws JAXBException, IOException {
        moneyConfigMock("BRA", 150d);
        usuarioByIdMock(1, new Comprador());
        usuarioByIdMock(2, new Vendedor());
        when(productService.internalFindById(Mockito.anyLong())).thenReturn(umProdutoCom(BigDecimal.ONE, "Banana", 2, 100));

        Response<String> response = compraService.newCompra(umCarrinhoDTOCom(umaCompraDTOCom(new Endereco(), BigDecimal.valueOf(100), 100)), umUsuarioAutenticadoCom(1L), "BRA");

        Assert.assertEquals(1, response.getErrors().size());
        Assert.assertEquals("Não foi possível realizar a compra pois o valor total a ser pago não atingiu o valor mínimo para realizar uma compra. Valor mínimo: 150.0", response.getErrors().get(0));
    }

    @Test
    public void deveSolicitarPagamentoViaPagSeguroSeACompraForNoBrasil() throws JAXBException, IOException {
        moneyConfigMock("BRA", 50);
        usuarioByIdMock(1, new Comprador());
        usuarioByIdMock(2, new Vendedor());
        when(productService.internalFindById(Mockito.anyLong())).thenReturn(umProdutoCom(BigDecimal.ONE, "Banana", 2, 100));
        when(enderecoService.saveEndereco(Mockito.any(Endereco.class))).thenReturn(umResponseCom(umEnderecoCom("1256789")));
        when(compraRepository.save(Mockito.any(Compra.class))).thenReturn(umaCompraCom(new Endereco()));
        when(carrinhoService.internalSave(Mockito.any(Carrinho.class))).thenReturn(umCarrinhoCom(1));
        when(pagSeguroService.efetuaPagamentoDeProdutos(Mockito.any(XmlBody.class))).thenReturn("código de pagamento");

        compraService.newCompra(umCarrinhoDTOCom(umaCompraDTOCom(new Endereco(), BigDecimal.valueOf(100), 100)), umUsuarioAutenticadoCom(1L), "BRA");

        Mockito.verify(pagSeguroService, Mockito.times(1)).efetuaPagamentoDeProdutos(Mockito.any());
        Mockito.verify(emisService, Mockito.never()).efetuaPagamentoDeProdutos(Mockito.any());
    }

    @Test
    public void deveSolicitarPagamentoViaEmisSeACompraForEmAngola() throws JAXBException, IOException {
        moneyConfigMock("AGO", 50);
        usuarioByIdMock(1, new Comprador());
        usuarioByIdMock(2, new Vendedor());
        when(productService.internalFindById(Mockito.anyLong())).thenReturn(umProdutoCom(BigDecimal.ONE, "Banana", 2, 100));
        when(enderecoService.saveEndereco(Mockito.any(Endereco.class))).thenReturn(umResponseCom(umEnderecoCom("1256789")));
        when(compraRepository.save(Mockito.any(Compra.class))).thenReturn(umaCompraCom(new Endereco()));
        when(carrinhoService.internalSave(Mockito.any(Carrinho.class))).thenReturn(umCarrinhoCom(1));

        compraService.newCompra(umCarrinhoDTOCom(umaCompraDTOCom(new Endereco(), BigDecimal.valueOf(100), 100)), umUsuarioAutenticadoCom(1L), "AGO");

        Mockito.verify(pagSeguroService, Mockito.never()).efetuaPagamentoDeProdutos(Mockito.any());
        Mockito.verify(emisService, Mockito.times(1)).efetuaPagamentoDeProdutos(Mockito.any());
    }

    @Test
    public void deveAlterarOStatusDoCarrinhoParaAguardandoConfirmacaoDaLojaQuandoOPagamentoForEfetivado_EMIS() {
        Carrinho carrinho = mock(Carrinho.class);
        when(carrinho.getStatusCompra()).thenReturn(StatusCompra.AGUARDANDO_PAGAMENTO);
        when(carrinhoService.internalFindById(anyLong())).thenReturn(carrinho);
        when((vendaRepository.findAllByCarrinhoId(anyLong()))).thenReturn(Collections.emptyList());
        when(taxaRepository.findById(anyLong())).thenReturn(Optional.of(new Taxa()));
        TransactionGPO transactionGPO = umaTransactionGPO(TransactionGPO.ACCEPTED);

        compraService.pagamentoAtualizadoEMIS(transactionGPO);

        verify(carrinho, times(1)).setStatusCompra(StatusCompra.AGUARDANDO_CONFIRMACAO);
    }

    @Test
    public void deveAtualizarOsValoresDeResumoDaVenda() {
        Carrinho carrinho = mock(Carrinho.class);
        when(carrinho.getStatusCompra()).thenReturn(StatusCompra.AGUARDANDO_PAGAMENTO);
        when(carrinhoService.internalFindById(anyLong())).thenReturn(carrinho);
        Venda venda1 = umaVenda(BigDecimal.valueOf(100d), 1);
        Venda venda2 = umaVenda(BigDecimal.valueOf(87.34), 2);
        when((userService.internalFindUserById(1))).thenReturn(umVendedorComEntregadoresProprio());
        when((userService.internalFindUserById(2))).thenReturn(umVendedorSemEntregadoresProprio());
        when((vendaRepository.findAllByCarrinhoId(anyLong()))).thenReturn(Arrays.asList(venda1, venda2));
        when(taxaRepository.findById(1L)).thenReturn(Optional.of(umaTaxa(7.0)));
        when(taxaRepository.findById(2L)).thenReturn(Optional.of(umaTaxa(2.5)));
        TransactionGPO transactionGPO = umaTransactionGPO(TransactionGPO.ACCEPTED);

        compraService.pagamentoAtualizadoEMIS(transactionGPO);

        Assert.assertEquals(7d, venda1.getTaxaFieldrightVenda().doubleValue(), 0);
        Assert.assertEquals(0d, venda1.getTaxaFieldrightEntrega().doubleValue(), 0);
        Assert.assertEquals(93d, venda1.getValorLiquido().doubleValue(), 0);
        Assert.assertEquals(6.1138, venda2.getTaxaFieldrightVenda().doubleValue(), 0);
        Assert.assertEquals(2.1835, venda2.getTaxaFieldrightEntrega().doubleValue(), 0);
        Assert.assertEquals(79.0427, venda2.getValorLiquido().doubleValue(), 0);

    }

    @Test
    public void deveAlterarOStatusDoCarrinhoParaCANCELADOQuandoOPagamentoForRejeitado_EMIS() {
        Carrinho carrinho = mock(Carrinho.class);
        when(carrinhoService.internalFindById(anyLong())).thenReturn(carrinho);
        TransactionGPO transactionGPO = umaTransactionGPO(TransactionGPO.REJECTED);

        compraService.pagamentoAtualizadoEMIS(transactionGPO);

        verify(carrinho, times(1)).setStatusCompra(StatusCompra.CANCELADA);
    }

    @NotNull
    private TransactionGPO umaTransactionGPO(String status) {
        TransactionGPO transactionGPO = new TransactionGPO("123");
        transactionGPO.setStatus(status);
        return transactionGPO;
    }

    private Compra umaCompraCom(Endereco endereco) {
        Compra compra = new Compra();
        compra.setEnderecoEntrega(endereco);
        return compra;
    }

    private Endereco umEnderecoCom(String cep) {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        return endereco;
    }

    private Carrinho umCarrinhoCom(long id) {
        Carrinho carrinho = new Carrinho();
        carrinho.setId(id);
        return carrinho;
    }

    private Response<Endereco> umResponseCom(Endereco endereco) {
        return new Response.Builder()
                .withData(endereco)
                .build();
    }

    private void usuarioByIdMock(long userId, Usuario usuario) {
        when(userService.internalFindUserById(userId)).thenReturn(usuario);
    }

    private UserAuthenticated umUsuarioAutenticadoCom(long id) {
        UserAuthenticated authenticated = new UserAuthenticated();
        authenticated.setId(id);
        return authenticated;
    }

    private void moneyConfigMock(String pais, double valorMinimo) {
        MoneyConfig moneyConfig = new MoneyConfig();
        moneyConfig.setValorMínimoCompra(valorMinimo);
        when(moneyConfigRepository.findByPaisCodigoLike(pais)).thenReturn(Optional.of(moneyConfig));
    }

    private Vendedor umVendedorCom(Endereco enderecoLoja, Boolean possuiEntregadores) {
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(possuiEntregadores);
        vendedor.setEndereco(enderecoLoja);
        return vendedor;
    }

    private Endereco geraEndereco(String cidade) {
        Endereco enderecoEntrega = new Endereco();
        enderecoEntrega.setCidade(cidade);
        return enderecoEntrega;
    }

    private Product umProdutoCom(BigDecimal preco, String nome, long vendedorId, int qtdDisponivel) {
        Product product = umProdutoCom(vendedorId, 0d);
        product.setName(nome);
        product.setPrice(preco);
        product.setQuantityAvailable(BigDecimal.valueOf(qtdDisponivel));
        product.setPictures(new ArrayList<>());
        return product;
    }

    private Product umProdutoCom(long vendedorId, double pesoCubado) {
        Product product = new Product();
        product.setVendedorId(vendedorId);
        product.setPesoCubado(pesoCubado);
        product.setPictures(new ArrayList<>());
        return product;
    }

    private CarrinhoDTO umCarrinhoDTOCom(CompraDTO... compraDTOS) {
        CarrinhoDTO carrinhoDTO = new CarrinhoDTO();
        carrinhoDTO.setCompras(Arrays.asList(compraDTOS));
        carrinhoDTO.setFormaPagamento(compraDTOS[0].getFormaPagamento());
        return carrinhoDTO;
    }

    private CompraDTO umaCompraDTOCom(Endereco enderecoEntrega, BigDecimal vlPago, int qtdComprada) {
        CompraDTO compraDTO = umaCompraDTOCom(enderecoEntrega);
        compraDTO.setQtdComprada(qtdComprada);
        compraDTO.setVlPago(vlPago);
        compraDTO.setFormaPagamento("CARTAO_CREDITO");
        return compraDTO;
    }

    private CompraDTO umaCompraDTOCom(Endereco enderecoEntrega) {
        CompraDTO compraDTO = new CompraDTO();
        compraDTO.setProductId(12L);
        compraDTO.setQtdComprada(4);
        compraDTO.setEnderecoEntrega(enderecoEntrega);
        compraDTO.setDistanciaEntrega("15 km");
        return compraDTO;
    }

    private void geraTabelaFrete(double minimo, double maximo, BigDecimal valor) {
        TabelaFrete tabelaFrete = new TabelaFrete();
        tabelaFrete.setMinimo(minimo);
        tabelaFrete.setMaximo(maximo);
        tabelaFrete.setValor(valor);

        when(tabelaFreteRepository.findAll()).thenReturn(Arrays.asList(tabelaFrete));
    }

    private Venda umaVenda(BigDecimal valorTotal, long vendedorId) {
        Venda venda = new Venda();
        venda.setVlTotal(valorTotal);
        venda.setVendedorId(vendedorId);

        return venda;
    }

    private Taxa umaTaxa(double percentual) {
        Taxa taxa = new Taxa();
        taxa.setPercentual(percentual);
        return taxa;
    }

    private Vendedor umVendedorSemEntregadoresProprio() {
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(false);
        return vendedor;
    }

    private Vendedor umVendedorComEntregadoresProprio() {
        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(true);
        return vendedor;
    }
}
