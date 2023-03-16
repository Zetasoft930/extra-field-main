package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.*;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.TabelaFrete;
import com.fieldright.fr.entity.dto.*;
import com.fieldright.fr.entity.emis.TransactionGPO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.mail.EMailSender;
import com.fieldright.fr.notification.PushSender;
import com.fieldright.fr.repository.*;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.*;
import com.fieldright.fr.util.PaisesSuportados;
import com.fieldright.fr.util.StringUtil;
import com.fieldright.fr.util.enums.FormaPagamento;
import com.fieldright.fr.util.enums.StatusCompra;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.exception.CountryNotSupportedException;
import com.fieldright.fr.util.mapper.CarrinhoMapper;
import com.fieldright.fr.util.mapper.CompraMapper;
import com.fieldright.fr.util.mapper.XmlCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

import static com.fieldright.fr.util.PaisesSuportados.BRASIL;

@Service
public class CompraServiceImpl implements CompraService {

	private final static String PERMISSION_DENIED_COMPRADOR_MESSAGE = "Não foi possível realizar a compra pois este "
			+ "usuário não possui autorização para esta operação";
	private final static String DIVERGENCIA_NO_VALOR_A_SER_PAGO_MESSAGE = "Não foi possível realizar a compra pois o valor a ser"
			+ " pago diverge do valor que precisa ser pago no produto: ";
	private final static String VALOR_TOTAL_MENOR_DO_QUE_O_MINIMO_MESSAGE = "Não foi possível realizar a compra pois o valor "
			+ "total a ser pago não atingiu o valor mínimo para realizar uma compra. Valor mínimo: ";
	private final static String QUANTIDADE_DISPONIVEL_INSUFICIENTE_MESSAGE = "Não foi possível realizar a compra pois não tem "
			+ "quantidade suficiente para sua compra do produto: ";
	private final static String CARRINHO_CANCELADO_POR_FALTA_PAGAMENTO_MESSAGE = "Não foi possível finalizar a compra pois "
			+ "ela foi cancelada devido o tempo de pagamento";
	private static final int TRANSACTION_STATUS_PAGA = 3;
	private static final int TRANSACTION_STATUS_DISPONIVEL = 4;
	private final static String QUANTIDADE_DISPONIVEL_INFERIOR_QTD_MINIMA = "Quantidade disponivel inferior a quantidade mínima para o produto: ";
	private final static String STOCK_ESGOTADO = "Estoque esgotado para o produto: ";
	private final static String QUANTIDADE_DISPONIVEL_IGUAL_A_UM = "Restou apenas uma unidade do produto: ";
	private final static String key = "fieldright/frete/gratis";


	@Autowired
	private PushSender pushSender;
	@Autowired
	private EMailSender eMailSender;
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CarrinhoMapper carrinhoMapper;
	@Autowired
	private CarrinhoService carrinhoService;
	@Autowired
	private VendaRepository vendaRepository;
	@Autowired
	private PagSeguroService pagSeguroService;
	@Autowired
	private EmisService emisService;
	@Autowired
	private CompraRepository compraRepository;
	@Autowired
	private EnderecoServiceImpl enderecoService;
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private MoneyConfigRepository moneyConfigRepository;
	@Autowired
	private TabelaFreteRepository tabelaFreteRepository;
	@Autowired
	private TaxaRepository taxaRepository;
	@Autowired
	private ParametrosRepository parametroRepository;
	@Autowired
	private PromocaoFreteRepository promocaoFreteRepository;

	@Autowired
	private UnidadeMedidaConverterService unidadeMedidaConverterService;

	public CompraServiceImpl(CompraRepository compraRepository, ProductService productService, UserService userService,
			TabelaFreteRepository tabelaFreteRepository, MoneyConfigRepository moneyConfigRepository,
			PagSeguroService pagSeguroService, EmisService emisService, EnderecoServiceImpl enderecoService,
			CarrinhoService carrinhoService, EMailSender eMailSender, VendaRepository vendaRepository,
			TaxaRepository taxaRepository) {
		this.compraRepository = compraRepository;
		this.productService = productService;
		this.userService = userService;
		this.tabelaFreteRepository = tabelaFreteRepository;
		this.moneyConfigRepository = moneyConfigRepository;
		this.pagSeguroService = pagSeguroService;
		this.emisService = emisService;
		this.enderecoService = enderecoService;
		this.carrinhoService = carrinhoService;
		this.eMailSender = eMailSender;
		this.vendaRepository = vendaRepository;
		this.taxaRepository = taxaRepository;
	}

	/**
	 * * Só usuários com perfil de comprador que pode comprar * PRA CADA PRODUTO *
	 * Verificar se a quantidade disponível para a compra é maior ou igual a
	 * quqntidade comprada * Verificar se o valor pago faz sentido com a quantidade
	 * comprada * O valor total do carrinho deve pegar da soma dos valo9res pagos de
	 * cada compra * O valor a ser pago deve ser maior ou igual ao valor mínimo
	 * definido (50R$) * fazer uma "reserva" dos produtos. * * Aumentar a quantidade
	 * reservada de cada produto * * Salvar todas as compras no banco * * Salvar o
	 * carrinho no banco com todas as compras * * 2 minuto depois da execução do
	 * método, deve ser verificada se o pagamento no carrinho foi efetuado * * caso
	 * não forem pagos (statusCompra == AGUARDANDO_PAGAMENTO) * * Remover a reserva
	 * de todos os produtos * * Alterar o statusCompra do Cariinho para 'CANCELADO'
	 * * Retornar o id do carrinho
	 *
	 * @param carrin
	 * @param authenticated
	 * @return
	 */
	@Override
	public Response<String> newCompra(CarrinhoDTO carrin, UserAuthenticated authenticated, String pais)
			throws IOException, JAXBException {
		try {
			Optional<MoneyConfig> configOptional = moneyConfigRepository.findByPaisCodigoLike(pais);
			validaPais(pais, configOptional);
			double valorMínimo = configOptional.get().getValorMínimoCompra();
			Usuario usuario = userService.internalFindUserById(authenticated.getId());

			if (usuario.getClass().equals(Comprador.class)) {
				List<Compra> compras = new ArrayList<>();
				List<String> errors = new ArrayList<>();
				BigDecimal vlTotal = gereCompras(carrin, usuario, compras, errors);
				if (existeErros(errors))
					return returnStringInResponse(HttpStatus.NOT_ACCEPTABLE, null, errors);
				if (valorDaCompraMenorQueOMinimoPermitido(valorMínimo, vlTotal))
					return returnStringInResponse(HttpStatus.NOT_ACCEPTABLE, null,
							Arrays.asList(VALOR_TOTAL_MENOR_DO_QUE_O_MINIMO_MESSAGE + valorMínimo));
				carrin.setVlTotal(vlTotal);
				List<Compra> comprasSalvas = reserveProdutosESalveCompras(compras);
				Long carrinhoId = gereESalveCarrinho(carrin, authenticated, comprasSalvas);
				userService.internalAdicioneComprasParaComprador(carrinhoId, authenticated.getId());
				String codigoPagamento = pagSeguroService.efetuaPagamentoDeProdutos(XmlCreator.createXmlBody((Comprador) usuario, compras, carrinhoId, carrin.getTaxaEntrega()));
				//String codigoPagamento = "CARTAO_DEBITO";
				carrinhoService.internalAddCodigoPagamento(carrinhoId, codigoPagamento);

				sendNotification(comprasSalvas);
				return returnStringInResponse(HttpStatus.OK, codigoPagamento, null);
			}
			return returnStringInResponse(HttpStatus.UNAUTHORIZED, null,
					Arrays.asList(PERMISSION_DENIED_COMPRADOR_MESSAGE));
		} catch (RuntimeException e) {
			return returnStringInResponse(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList(e.getMessage()));
		}
	}

	private String efetuaPagamento(CarrinhoDTO carrinho, Comprador usuario, List<Compra> compras, Long carrinhoId,
			String pais) throws IOException, JAXBException {
		String codigoPagamento = "";
		if (pais.equalsIgnoreCase(BRASIL)) {
			codigoPagamento = efetuaPagamentoBrasil(carrinho, usuario, compras, carrinhoId);
		} else if (pais.equalsIgnoreCase(PaisesSuportados.ANGOLA)) {
			carrinho.setId(carrinhoId);
			codigoPagamento = efetuaPagamentoAngola(carrinho);
		}
		return codigoPagamento;
	}

	private String efetuaPagamentoAngola(CarrinhoDTO carrinho) {
		String codigoPagamento = emisService.efetuaPagamentoDeProdutos(carrinho);
		carrinhoService.internalAddCodigoPagamento(carrinho.getId(), codigoPagamento);
		return codigoPagamento;
	}

	private String efetuaPagamentoBrasil(CarrinhoDTO carrin, Comprador usuario, List<Compra> compras, Long carrinhoId)
			throws IOException, JAXBException {
		String codigoPagamento;
		codigoPagamento = pagSeguroService.efetuaPagamentoDeProdutos(
				XmlCreator.createXmlBody(usuario, compras, carrinhoId, carrin.getTaxaEntrega()));
		carrinhoService.internalAddCodigoPagamento(carrinhoId, codigoPagamento);
		return codigoPagamento;
	}

	private boolean valorDaCompraMenorQueOMinimoPermitido(double valorMínimo, BigDecimal vlTotal) {
		return vlTotal.compareTo(BigDecimal.valueOf(valorMínimo)) == -1;
	}

	private boolean existeErros(List<String> errors) {
		return errors.size() != 0;
	}

	private BigDecimal gereCompras(CarrinhoDTO carrin, Usuario usuario, List<Compra> compras, List<String> errors) {
		BigDecimal vlTotal = BigDecimal.valueOf(0).setScale(2);
		for (CompraDTO c : carrin.getCompras()) {
			compras.add(gereCompra(c, usuario, errors));

			vlTotal = vlTotal.add(c.getVlPago()).setScale(2);
		}
		return vlTotal;
	}

	private void validaPais(String pais, Optional<MoneyConfig> configOptional) {
		if (configOptional.isEmpty()) {
			throw new CountryNotSupportedException("Compras não são permitidas para o país informado: " + pais);
		}
	}

	private Long gereESalveCarrinho(CarrinhoDTO carrinhoDTO, UserAuthenticated comprador, List<Compra> compras) {
		Carrinho carrinho = new Carrinho.Builder().withCompradorId(comprador.getId()).withCompras(compras)
				.withEnderecoEntrega(carrinhoDTO.getCompras().get(0).getEnderecoEntrega().toString())
				.withFormaPagamento(FormaPagamento.valueOf(carrinhoDTO.getFormaPagamento().toUpperCase()))
				.withVlTotal(carrinhoDTO.getVlTotal()).withTaxaEntrega(carrinhoDTO.getTaxaEntrega())
				.withStatus(StatusCompra.AGUARDANDO_PAGAMENTO).build();
		return carrinhoService.internalSave(carrinho).getId();
	}

	private List<Compra> reserveProdutosESalveCompras(List<Compra> compras) {
		/**
		 * PRA CADA COMPRA * Recuperar o produto pelo productId da compra * Verificar se
		 * a quantidade disponível para a compra é maior ou igual a quqntidade comprada
		 * * Aumentar a quantidade reservada do produto * Salvar (atualizar) o produto *
		 * Salvar a compra RETORNO * Lista das compras salvas
		 */

		List<Compra> compraList = new ArrayList<>();
		for (Compra compra : compras) {
			Product product = productService.internalFindById(compra.getProductId());

			BigDecimal novaQtdCompra =  getQtCompraCalc(product.getUnidadeMedida(),compra.getUnidadeMedida(),compra.getQtdComprada());

			lancaExecaoCasoNaoTiverProdutoNoEstoque(product,novaQtdCompra);
			product.setQtdReservada(product.getQtdReservada().add(novaQtdCompra));
			productService.internalSave(product);

			Endereco enderecoEntrega = enderecoService.saveEndereco(compra.getEnderecoEntrega()).getData();
			compra.setEnderecoEntrega(enderecoEntrega);
			compraList.add(compraRepository.save(compra));
		}
		return compraList;
	}

	private void lancaExecaoCasoNaoTiverProdutoNoEstoque(Product product,BigDecimal novaQuatidade) {


		String error = confereQuantidadeDisponivel(product, novaQuatidade);
		if (error != null)
			throw new RuntimeException(error);
	}

	private Response<Long> returnIdInResponse(HttpStatus status, Long aLong, List<String> errors) {
		return new Response.Builder().withStatus(status).withData(aLong).withErrors(errors).build();
	}

	private Response<String> returnStringInResponse(HttpStatus status, String retorno, List<String> errors) {
		return new Response.Builder().withStatus(status).withData(retorno).withErrors(errors).build();
	}

	private Response<CarrinhoDTO> returnCarrinhoInResponse(HttpStatus status, CarrinhoDTO carrinhoDTO,
			List<String> errors) {
		return new Response.Builder().withStatus(status).withData(carrinhoDTO).withErrors(errors).build();
	}

	@Override
	public void pagamentoAtualizadoPagSeguro(String notificationCode) {
		/**
		 * Conferir se o usuário logado é o mesmo que cadastrou a compra no método
		 * anterior Conferir que ainda não passaram os 2 minutos de confirmação de
		 * pagamento (statusCompra == AGUARDANDO_PAGAMENTO) Diminuir a quantidade
		 * disponível e reservada do produto comprado Atualizar o status do carrinho e
		 * das compras para "AGUARDANDO_COMFIRMACAO" Gerar uma venda para cada vendedor
		 * (pelo vendaService) Retornar o carrinho Enviar um e-mail de confirmação de
		 * compra pro comprador Enviar um e-mail de nova venda pro vendedor
		 *
		 */

		// TODO
		// Dentro da variável transaction >> paymantMethod>>Type tem a forma de
		// pagamento que foi utilizada (Pix credito, boleto...)
		PagSeguroServiceImpl.XmlTransactionResponse transactionResponse = pagSeguroService
				.consultaPagamento(notificationCode);
		if (transactionResponse.getStatus() == TRANSACTION_STATUS_PAGA
				|| transactionResponse.getStatus() == TRANSACTION_STATUS_DISPONIVEL) {
			processaPagamentoDaCompra(transactionResponse.getReference());
		}
	}

	private void processaPagamentoDaCompra(long reference) {
		Carrinho carrinho = carrinhoService.internalFindById(reference);
		if (carrinho.getStatusCompra().equals(StatusCompra.AGUARDANDO_PAGAMENTO)) {
			for (Compra c : carrinho.getCompras()) {
				productService.internalUpdateForNewCompra(c.getProductId(), c.getQtdComprada(),c.getUnidadeMedida());
				c.setStatus(StatusCompra.AGUARDANDO_CONFIRMACAO);
				internalNewVenda(c, reference, c.getVendedorId());
			}
			carrinho.setStatusCompra(StatusCompra.AGUARDANDO_CONFIRMACAO);
			compraRepository.saveAll(carrinho.getCompras());
			carrinhoService.internalSave(carrinho);
			atualizeValoresDasVendas(reference);
			eMailSender.sendCompraEfetuada(userService.internalFindUserById(carrinho.getCompradorId()));
		}
	}

	private void atualizeValoresDasVendas(long carrinhoId) {
		List<Venda> vendas = vendaRepository.findAllByCarrinhoId(carrinhoId);
		double peTaxaFieldrightVenda = taxaRepository.findById(1L).get().getPercentual();
		double peTaxaFieldrightEntrega = taxaRepository.findById(2L).get().getPercentual();

		for (Venda venda : vendas) {
			BigDecimal taxaFieldrightVenda = calculeValorProporcional(venda.getVlTotal(), peTaxaFieldrightVenda);
			BigDecimal taxaFieldrightEntrega = BigDecimal.ZERO;
			Vendedor vendedor = (Vendedor) userService.internalFindUserById(venda.getVendedorId());
			if (!vendedor.getPossuiEntregadores()) {
				taxaFieldrightEntrega = calculeValorProporcional(venda.getVlTotal(), peTaxaFieldrightEntrega);
			}
			venda.setTaxaFieldrightVenda(taxaFieldrightVenda);
			venda.setTaxaFieldrightEntrega(taxaFieldrightEntrega);

			BigDecimal vlTotal = venda.getVlTotal();
			venda.setValorLiquido(vlTotal.subtract(taxaFieldrightVenda.add(taxaFieldrightEntrega)));
			vendaRepository.save(venda);
		}
	}

	private BigDecimal calculeValorProporcional(BigDecimal valorBase, double percentual) {
		return valorBase.multiply(new BigDecimal(percentual)).divide(new BigDecimal(100));
	}

	private void internalNewVenda(Compra compra, long carrinhoId, long vendedorId) {
		/**
		 * Caso tiver várias compras de muitos produtos do mesmo vendedor no mesmo
		 * carrinho, o sistema deve gerar apenas uma única venda para aquelas compras.
		 *
		 * * Ver se tem alguma venda com status "NOVO" na lista de vendas do vendedor *
		 * Caso tiver: * Verificar se o carrinho da venda é o mesmo que foi passado como
		 * param * Se for o mesmo * Adicione a compra na lista de compras da venda *
		 * Atualize o valor total da venda * Atualize a venda * Se não tiver nenhuma
		 * venda "NOVO" com o mesmo carrinho, criar uma nova venda com os passos abaixo
		 * * Caso não tiver: * Criar uma nova venda * Adicionar a compra na lista de
		 * compras da venda * Adicionar a venda na lista das vendas do vendedor * Enviar
		 * um email de nova venda 1 minuto depois * Notificação
		 */

		List<Venda> vendaNovas = vendaRepository.findAllByVendedorIdAndStatusEquals(vendedorId, StatusVenda.NOVA);
		Vendedor vendedor = (Vendedor) userService.internalFindUserById(vendedorId);

		for (Venda venda : vendaNovas) {
			if (venda.getCarrinhoId() == carrinhoId) {
				venda.getCompras().add(compra);
				venda.setVlTotal(venda.getVlTotal().add(compra.getVlPago()));
				vendaRepository.save(venda);
				return;
			}
		}
		Venda v = gereNovaVenda(compra, carrinhoId, vendedorId);
		Venda venda = vendaRepository.save(v);
		vendedor.getVendas().add(venda);
		userService.internalUpdateUser(vendedor);
		pushSender.avisaNovaVendaEfetuada(vendedorId);
		eMailSender.sendNovaVendaEfetuada(userService.internalFindUserById(vendedorId));
	}

	private Venda gereNovaVenda(Compra compra, Long carrinhoId, Long vendedorId) {
		Vendedor vendedor = (Vendedor) userService.internalFindUserById(vendedorId);
		Venda venda = new Venda.Builder().withCarrinhoId(carrinhoId).withVendedorId(vendedorId)
				.withVendedorName(vendedor.getFullName()).withVendedorPhone(vendedor.getPhone())
				.withCompradorId(compra.getCompradorId()).withCompradorName(compra.getCompradorName())
				.withCompradorPhone(compra.getCompradorPhone()).withEnderecoEntrega(compra.getEnderecoEntrega())
				.withVlTotal(compra.getVlPago()).withFormaPagamento(compra.getFormaPagamento())
				.withStatus(StatusVenda.NOVA).build();
		venda.getCompras().add(compra);

		return venda;
	}

	private Compra gereCompra(CompraDTO dto, Usuario comprador, List<String> errors) {
		/**
		 * Verificar se a quantidade disponível para a compra é maior ou igual a
		 * quqntidade comprada Verificar se o valor pago faz sentido com a quantidade
		 * comprada
		 */
		Product product = productService.internalFindById(dto.getProductId());
		Usuario vendedor = userService.internalFindUserById(product.getVendedorId());
		List<String> errorList = new ArrayList<>();

		//Calcula novo de acordo com a unidade de medida.aplicacao do calculo 3 simples
		BigDecimal novaQtdComprada = getQtCompraCalc(product.getUnidadeMedida(),dto.getUnidadeMedida(),dto.getQtdComprada());

		errorList.add(confereQuantidadeDisponivel(product, novaQtdComprada));
		errorList.add(confereValorPago(product, novaQtdComprada, dto.getVlPago()));

		for (String err : errorList)
			if (err != null)
				errors.add(err);

		return new Compra.Builder()
				.withProductId(product.getId())
				.withProductName(product.getName())
				.withProductDescription(product.getDescription())
				.withProductPrice(dto.getVlPago().divide(BigDecimal.valueOf(dto.getQtdComprada())))
				.withVendedorId(vendedor.getId()).withVendedorName(vendedor.getFullName())
				.withVendedorPhone(vendedor.getPhone()).withCompradorId(comprador.getId())
				.withCompradorName(comprador.getFullName()).withCompradorPhone(comprador.getPhone())
				.withVlPago(dto.getVlPago())
				.withQtdComprada(dto.getQtdComprada())
				.withUnidadeMedida(dto.getUnidadeMedida())
				.withEnderecoLoja(product.getEnderecoLoja())
				.withEnderecoEntrega(dto.getEnderecoEntrega())
				.withDistanciaEntrega(dto.getDistanciaEntrega())
				.withStatus(StatusCompra.AGUARDANDO_PAGAMENTO)
				.withUnidadeMedida(dto.getUnidadeMedida())
				.withObservacao(dto.getObservacao()).withFracao(dto.getFracao())
				.withFormaPagamento(FormaPagamento.valueOf(dto.getFormaPagamento().toUpperCase()))
				.addProductPictures(retornaImagensProduto(product)).build();
	}

	private List<String> retornaImagensProduto(Product product) {
		List<String> pictures = new ArrayList<String>();
		pictures.addAll(product.getPictures());
		return pictures;
	}

	private String confereValorPago(Product product, BigDecimal qtdComprada, BigDecimal vlPago) {
		BigDecimal vlCerto = (product.getPrice().multiply(qtdComprada));

		return (vlCerto.compareTo(vlPago)) == 0 ? null : DIVERGENCIA_NO_VALOR_A_SER_PAGO_MESSAGE + product.getName();
	}



	/*private String confereQuantidadeDisponivel(Product product, int qtdComprada) {
		return (qtdComprada <= product.qtdDisponivelParaCompra().doubleValue()) ? null
				: QUANTIDADE_DISPONIVEL_INSUFICIENTE_MESSAGE + product.getName();
	}*/

	private String confereQuantidadeDisponivel(Product product, BigDecimal qtdComprada) {

		return (qtdComprada.doubleValue() <= product.qtdDisponivelParaCompra().doubleValue()) ? null
				: QUANTIDADE_DISPONIVEL_INSUFICIENTE_MESSAGE + product.getName();
	}

	public BigDecimal getQtCompraCalc(String unidadeProduto,String unidadeCompra,int qtdComprada){



		if(unidadeCompra != null) {

			if (!unidadeProduto.equalsIgnoreCase(unidadeCompra)) {

				UnidadeMedidaConverter unidadeMedidaConverter = unidadeMedidaConverterService.findByUnidadeSimbolo(unidadeCompra, unidadeProduto);

				if (unidadeMedidaConverter != null) {


					BigDecimal qtd = unidadeMedidaConverter.getEquivale().multiply(BigDecimal.valueOf(qtdComprada));

					return qtd;
				}

			}

		}

	  return BigDecimal.valueOf(qtdComprada);

	}

	@Override
	public Response getPedidoByUserAndStatus(Long userIdLoja, StatusVenda status, Pageable pageable) {


		Page<CompraDTO> dtos=null;
		Page<Compra> compras=this.compraRepository.findVendaByVendedorAndStatus(userIdLoja,status.getText(),pageable);


		dtos = compras.map(compra -> {

			return CompraMapper.toCompraDTO(compra);

		});

		return new Response.Builder()
				.withStatus(HttpStatus.OK)
				.withData(dtos)
				.withErrors(null)
				.build();

	}

	@Override
	public Response<CompraDTO> findById(long id) {
		return null;
	}

	@Override
	public void internalCanceleCompra(Compra compra) {
		compra.setStatus(StatusCompra.CANCELADA);
		compraRepository.save(compra);
	}

	@Override
	public void internalConfirmeCompras(List<Compra> compras) {
		for (Compra compra : compras)
			compra.setStatus(StatusCompra.EM_PREPARACAO);
		compraRepository.saveAll(compras);
	}

	@Override
	public void internalCanceleCompras(List<Compra> compras) {
		for (Compra compra : compras)
			compra.setStatus(StatusCompra.CANCELADA);
		compraRepository.saveAll(compras);
		productService.internalUpdateForComprasCanceladas(compras);
	}

	/**
	 * Alterar os status de todas as compras para "A CAMINHO" Enviar notificação
	 * para o comprador
	 *
	 * @param compras
	 */
	@Override
	public void internalComprasACaminho(List<Compra> compras) {
		for (Compra compra : compras)
			compra.setStatus(StatusCompra.A_CAMINHO);
		compraRepository.saveAll(compras);

		Compra compra = compras.get(0);
		pushSender.avisaComprasACaminho(compra.getCompradorId(), compra.getVendedorName());
	}

	/**
	 * Alterar os status de todas as compras para "ENTREGUE"
	 *
	 * @param compras
	 */
	@Override
	public void internalComprasFinalizadas(List<Compra> compras) {
		for (Compra compra : compras)
			compra.setStatus(StatusCompra.ENTREGUE);
		compraRepository.saveAll(compras);
	}

	@Override
	public Response<HttpStatus> evaluate(Avaliacao avaliacao, long lojaId, UserAuthenticated authenticated) {
		/**
		 * * Só comprador pode avaliar * Só pode ter entre 0 - 5 estrelas
		 */
		Usuario comprador = userService.internalFindUserById(authenticated.getId());
		Vendedor loja = (Vendedor) userService.internalFindUserById(lojaId);
		List<String> erros = new ArrayList<>();

		if (comprador.getClass().equals(Comprador.class)) {
			if (avaliacao.getEstrelas() <= 5 && avaliacao.getEstrelas() >= 0) {
				avaliacao.setAvaliadorId(authenticated.getId());
				Avaliacao avaliacaoSalva = avaliacaoRepository.save(avaliacao);
				loja.getAvaliacoes().add(avaliacaoSalva);
				userService.internalUpdateUser(loja);
				return returnHttpStatusInResponse(HttpStatus.OK, null);
			} else {
				erros.add("A quantidade de estrelas passada não é vpalida");
			}
		} else {
			erros.add("Avaliações são permitidas somente para compradores");
		}
		return returnHttpStatusInResponse(HttpStatus.UNAUTHORIZED, erros);
	}

	@Override
	/**
	 * Somente comprandores possuiem compras
	 */
	public Response<List<CarrinhoDTO>> findAll(UserAuthenticated authenticated) {
		Comprador comprador = (Comprador) userService.internalFindUserById(authenticated.getId());
		return returnCarrinhoListInResponse(HttpStatus.OK, carrinhoMapper.toCarrinhoDTOList(comprador.getCompras()),
				null);
	}

	@Override
	/**
	 * Confere se ainda está com status AGUARDANDO_PAGAMENTO se sim: * Remover a
	 * reserva de todos os produtos * Alterar o statusCompra para 'CANCELADO' para
	 * cada compra * Alterar o statusCompra do Cariinho para 'CANCELADO'
	 */
	public void canceleComprasNaoPagas() {
		List<Carrinho> carrinhos = carrinhoService.findAllAguardandoPagamento();

		for (Carrinho carrinho : carrinhos) {
			Date dataVencimento = getDataVencimentoPagamento(carrinho.getCompras().get(0).getCreatedAt());
			if (dataVencimento.before(Calendar.getInstance().getTime())) {
				for (Compra compra : carrinho.getCompras()) {

					productService.internalCanceleReservaProduto(compra.getProductId(), compra.getQtdComprada(),compra.getUnidadeMedida());
					this.internalCanceleCompra(compra);
				}
				carrinhoService.internalCanceleCarrinho(carrinho);
			}
		}
	}

	@Override
	public Response<BigDecimal> calculeFrete(CarrinhoDTO carrinho) {
		return calculeFrete(carrinho.getCompras());
	}

	@Override
	public void pagamentoAtualizadoEMIS(TransactionGPO transactionGPO) {
		if (transactionGPO.getStatus().equals(TransactionGPO.ACCEPTED)) {
			processaPagamentoDaCompra(Long.valueOf(transactionGPO.getReferenceId()));
		} else {
			processaCancelamentoDaCompra(transactionGPO.getReferenceId());
		}
	}

	private void processaCancelamentoDaCompra(String referenceId) {
		long reference = Long.valueOf(referenceId);
		Carrinho carrinho = carrinhoService.internalFindById(reference);
		for (Compra c : carrinho.getCompras()) {
			productService.internalCanceleReservaProduto(c.getProductId(), c.getQtdComprada(),c.getUnidadeMedida());
			c.setStatus(StatusCompra.CANCELADA);
		}
		carrinho.setStatusCompra(StatusCompra.CANCELADA);
		compraRepository.saveAll(carrinho.getCompras());
		carrinhoService.internalSave(carrinho);
	}

	/**
	 * Para cada vendedor: * Recuperar suas compras * Recuperar a distancia de
	 * entrega * Para cada compra: * Somar o peso cubado total daquela loja *
	 * Recupera o valor relacionado ao peso cubado total das compras daquela loja. *
	 * Calcular o frete final: somar o valor do peso cubado com a distancia de
	 * entrega
	 *
	 * @param compras
	 * @return
	 */
	private Response<BigDecimal> calculeFrete(List<CompraDTO> compras) {
		Map<Long, List<CompraDTO>> comprasPorLoja = separeComprasPorLojas(compras);
		Iterator iterator = comprasPorLoja.entrySet().iterator();
		BigDecimal valorFrete = new BigDecimal(0.00);
		BigDecimal totalCompra = new BigDecimal(0.00);
		BigDecimal value = getValueFrete();
		
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			List<CompraDTO> comprasDaLoja = (List<CompraDTO>) entry.getValue();
			totalCompra=comprasDaLoja.get(0).getVlPago();
			if (!(lojaPossuiEntregadoresProprio((Long) entry.getKey()))) {
				double distanciaEntrega = getDistanciaEntrega(comprasDaLoja.get(0));
				BigDecimal valorTabelaFrete = retornaValorTabelaFrete(comprasDaLoja);
				valorFrete = valorFrete.add(valorTabelaFrete.add(new BigDecimal(distanciaEntrega)));
			}
		}
		
		valorFrete = valorFrete.subtract(getFretePromotion(valorFrete));
		if (totalCompra.compareTo(value) >= 0) {
			valorFrete = BigDecimal.ZERO;
		}
		return returnHttpBigDecimalInResponse(HttpStatus.OK, valorFrete.round(new MathContext(4, RoundingMode.UP)),
				null);
	}

	private boolean lojaPossuiEntregadoresProprio(Long lojaId) {
		Vendedor vendedor = (Vendedor) userService.internalFindUserById(lojaId);
		return vendedor.getPossuiEntregadores();
	}

	@Nullable
	private BigDecimal retornaValorTabelaFrete(List<CompraDTO> comprasDaLoja) {
		double pesoCubadoTotal = 0;

		for (CompraDTO compra : comprasDaLoja) {
			Product product = productService.internalFindById(compra.getProductId());
			pesoCubadoTotal += (product.getPesoCubado() * compra.getQtdComprada().doubleValue());
		}
		return getValorEmRelacaoAoPesoCubado(pesoCubadoTotal);
	}

	@NotNull
	private Map<Long, List<CompraDTO>> separeComprasPorLojas(List<CompraDTO> compras) {
		Map<Long, List<CompraDTO>> comprasPorLoja = new HashMap<>();

		for (CompraDTO compra : compras) {
			Product product = productService.internalFindById(compra.getProductId());
			long vendedorId = product.getVendedorId();
			if (comprasPorLoja.containsKey(vendedorId)) {
				List<CompraDTO> compraDTOS = new ArrayList<>();
				compraDTOS.addAll(comprasPorLoja.get(vendedorId));
				compraDTOS.add(compra);
				comprasPorLoja.replace(vendedorId, compraDTOS);
			} else {
				comprasPorLoja.put(vendedorId, Arrays.asList(compra));
			}
		}
		return comprasPorLoja;
	}

	private BigDecimal getValorEmRelacaoAoPesoCubado(double pesoCubado) {
		List<TabelaFrete> tabelaFretes = tabelaFreteRepository.findAll();
		for (TabelaFrete tabelaFrete : tabelaFretes) {
			if (tabelaFrete.correspondeCom(pesoCubado)) {
				return tabelaFrete.getValor();
			}
		}
		return null;
	}

	private double getDistanciaEntrega(CompraDTO compra) {
		String distanciaEntrega = compra.getDistanciaEntrega();
		String s = StringUtil.getTextBeforeElement(distanciaEntrega.toLowerCase(), " km");
		return Double.valueOf(s);
	}

	private boolean entregaParaMesmaCidadeQueLoja(Long lojaId, CompraDTO compra) {
		Vendedor vendedor = (Vendedor) userService.internalFindUserById(lojaId);
		return vendedor.getEndereco().getCidade().equalsIgnoreCase(compra.getEnderecoEntrega().getCidade());
	}

	@NotNull
	private Date getDataVencimentoPagamento(Timestamp dataBase) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataBase);
		calendar.add(Calendar.DAY_OF_YEAR, 15);
		return calendar.getTime();
	}

	private Response<BigDecimal> returnHttpBigDecimalInResponse(HttpStatus status, BigDecimal bigDecimal,
			List<String> errors) {
		return new Response.Builder().withStatus(status).withData(bigDecimal).withErrors(errors).build();
	}

	private Response<HttpStatus> returnHttpStatusInResponse(HttpStatus status, List<String> errors) {
		return new Response.Builder().withStatus(status).withData(status).withErrors(errors).build();
	}

	private Response<List<CarrinhoDTO>> returnCarrinhoListInResponse(HttpStatus status,
			List<CarrinhoDTO> carrinhoDTOList, List<String> errors) {
		return new Response.Builder().withStatus(status).withData(carrinhoDTOList).withErrors(errors).build();
	}

	public void compraPaga_TEST(long carrinhoId) {
		processaPagamentoDaCompra(carrinhoId);
	}

	public void sendNotification(List<Compra> compras) {
		BigDecimal stock_disponivel;
				int min_stock;
		for (Compra compra : compras) {
			Product product = productService.internalFindById(compra.getProductId());
			String name = product.getName();
			stock_disponivel = product.qtdDisponivelParaCompra();
			min_stock = product.getMin_stock();
			if (stock_disponivel.doubleValue() <= min_stock && stock_disponivel.doubleValue() > 1 && min_stock > 1) {
				pushSender.avisaEstoque(compra.getVendedorId(), QUANTIDADE_DISPONIVEL_INFERIOR_QTD_MINIMA + name);
			} else if (stock_disponivel.doubleValue() == 1) {
				pushSender.avisaEstoque(compra.getVendedorId(), QUANTIDADE_DISPONIVEL_IGUAL_A_UM + name);
			} else if (stock_disponivel.doubleValue() == 0) {
				pushSender.avisaEstoque(compra.getVendedorId(), STOCK_ESGOTADO + name);
			} else {
			pushSender.avisaEstoque(compra.getVendedorId(), "Notificacao de teste para o produto: "+name);
			}
		}
	}

	public BigDecimal getValueFrete() {
		try {
			return new BigDecimal(parametroRepository.findValueByKey(key));
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}

	}
	
	public BigDecimal getFretePromotion(BigDecimal frete) {
		BigDecimal value;
		try {
			value = promocaoFreteRepository.findFretePromotion(JwtUserUtil.getUserAuthenticated().getId());
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
		return frete.multiply(value != null ? value : BigDecimal.ZERO);
	}

	@Override
	public void sendPush(UserAuthenticated authenticated, String mensagem) {
		pushSender.avisaEstoque(authenticated.getId(), mensagem);

	}

	@Override
	public Response<BigDecimal> price(CompraDTO dto) {
		BigDecimal newPrice = dto.getFracao().multiply(dto.getProductPrice());
		//BigDecimal vlCerto = (BigDecimal.valueOf(dto.getQtdComprada()).multiply(value));
		return returnHttpBigDecimalInResponse(HttpStatus.OK, newPrice.round(new MathContext(4, RoundingMode.UP)), null);

	}


	@Override
	public Response<PrecoDTO> newprice(ProductPriceDTO productPriceDTO) {

		try{

			Product product = productService.findById(productPriceDTO.getProductId());
			PrecoDTO precoDTO = null;


			if (product != null) {


				precoDTO = new PrecoDTO(product.getPrice(),BigDecimal.ZERO,product.getUnidadeMedida(),productPriceDTO.getUnidadeMedida());

                if(!product.getUnidadeMedida().equalsIgnoreCase(productPriceDTO.getUnidadeMedida())) {

					UnidadeMedidaConverter unidadeMedidaConverter =
							unidadeMedidaConverterService
									.findByUnidadeSimbolo(
											product.getUnidadeMedida(),
											productPriceDTO.getUnidadeMedida()
									);

					if (unidadeMedidaConverter != null) {

						if (unidadeMedidaConverter.getEquivale().doubleValue() > 0) {
							BigDecimal novoPreco = product.getPrice().divide(unidadeMedidaConverter.getEquivale());
							precoDTO.setNovoPreco(novoPreco);
						}

					}
				}else{
					precoDTO.setNovoPreco(product.getPrice());
				}

			}



			return new Response.Builder()
					.withStatus(HttpStatus.OK)
					.withData(precoDTO)
					.withErrors(null)
					.build();

		}catch (Exception ex){
			return new Response.Builder()
					.withStatus(HttpStatus.NOT_FOUND)
					.withData(null)
					.withErrors(Arrays.asList(ex.getMessage()))
					.build();
		}
	}



}
