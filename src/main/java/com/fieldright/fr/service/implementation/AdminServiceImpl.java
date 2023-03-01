package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.Depoimento;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.SuperCategory;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.entity.dto.CategoriaDTO;
import com.fieldright.fr.entity.dto.CompraDTO;
import com.fieldright.fr.entity.dto.ContaDTO;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.dto.ProductDTO;
import com.fieldright.fr.entity.dto.PromocaoFreteDTO;
import com.fieldright.fr.entity.dto.SuperCategoryDTO;
import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.dto.VendaDTO;
import com.fieldright.fr.entity.pagSeguro.TransferInfos;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.mail.EMailSender;
import com.fieldright.fr.notification.PushSender;
import com.fieldright.fr.repository.AvaliacaoRepository;
import com.fieldright.fr.repository.CategoryRepository;
import com.fieldright.fr.repository.CompraRepository;
import com.fieldright.fr.repository.ContaRepository;
import com.fieldright.fr.repository.DepoimentoRepository;
import com.fieldright.fr.repository.EntregaRepository;
import com.fieldright.fr.repository.MoneyConfigRepository;
import com.fieldright.fr.repository.ProductRepository;
import com.fieldright.fr.repository.PromocaoFreteRepository;
import com.fieldright.fr.repository.RemuneracaoEntregadoresConfigRepository;
import com.fieldright.fr.repository.SuperCategoryRepository;
import com.fieldright.fr.repository.TaxaRepository;
import com.fieldright.fr.repository.TransferInfosRepository;
import com.fieldright.fr.repository.UserRepository;
import com.fieldright.fr.repository.VendaRepository;
import com.fieldright.fr.repository.VendedorRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.*;
import com.fieldright.fr.util.enums.StatusCompra;
import com.fieldright.fr.util.enums.StatusDepoimento;
import com.fieldright.fr.util.exception.CountryNotSupportedException;
import com.fieldright.fr.util.exception.PermissionDeniedException;
import com.fieldright.fr.util.exception.UserAlreadyExistException;
import com.fieldright.fr.util.mapper.CategoryMapper;
import com.fieldright.fr.util.mapper.CompraMapper;
import com.fieldright.fr.util.mapper.EntregaMapper;
import com.fieldright.fr.util.mapper.ProductMapper;
import com.fieldright.fr.util.mapper.PromocaoFreteMapper;
import com.fieldright.fr.util.mapper.SuperCategoryMapper;
import com.fieldright.fr.util.mapper.UserMapper;
import com.fieldright.fr.util.mapper.VendaMapper;
import com.fieldright.fr.util.password.Bcrypt;
import com.fieldright.fr.util.validador.ValidadorDocumentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final String REGISTER_NOT_FOUND_MESSAGE = "Não foi encontrado registros com o id passado";
    private static final String SALDO_INSUFICIENTE_MESSAGE = "O seu saldo no PagSeguro é insuficiente para realizar esse pagamento!";
    private static final String CONTA_NAO_PREENCHIDA_MESSAGE = "O usuário informado não possui informações bancária!";
    private static final String CONTA_SEM_EMAIL_PAGSEGURO = "O usuário informado não possui conta válida no PagSeguro! \n O pagamento não pode ser feito por aqui!";
    @Value("${src.images.url}")
    private String imageUrl;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PushSender pushSender;
    @Autowired
    private VendaMapper vendaMapper;
    @Autowired
    private EMailSender mailSender;
    @Autowired
    private ContaService contaService;
    @Autowired
    private CompraMapper compraMapper;
    @Autowired
    private CompraService compraService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private EntregaMapper entregaMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaxaRepository taxaRepository;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private PagSeguroService pagSeguroService;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    @Autowired
    private ValidadorDocumentos validadorDocumentos;
    @Autowired
    private MoneyConfigRepository moneyConfigRepository;
    @Autowired
    private TransferInfosRepository transferInfosRepository;
    @Autowired
    private RemuneracaoEntregadoresConfigRepository remuneracaoEntregadoresConfigRepository;
    @Autowired
    private DepoimentoRepository depoimentoRepository;
    @Autowired
	private SuperCategoryMapper categoryMapper;
    @Autowired
    private SuperCategoryRepository superCategoryRepository;
	@Autowired
	private PromocaoFreteRepository promocaoFreteRepository;
	
	@Autowired
	private PromocaoFreteMapper promocaoFreteMapper;

    @Autowired
    private HistoricoPagamentoService historicoPagamentoService;

    /**
     * Caso o usuário a ativar for um vendedor, adicionar 1 mês na data da próxima desativação.
     * Compradores e motoristas não possuem data de próxima desativação.
     *
     * @param userId
     * @param authenticated
     * @return
     */
    @Override
    public Response<List<UserDTO>> ativeUsuario(long userId, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Usuario usuario = userRepository.findById(userId);
            if (usuario.getClass() == Vendedor.class) {
                adicioneUmMesNaDataProximaDesativacao((Vendedor) usuario);
            }
            usuario.setActive(true);
            userRepository.save(usuario);
            mailSender.sendCadastroAtivado(usuario);
            return findAllUser(authenticated);
        } catch (PermissionDeniedException e) {
            return returnListUserInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    private void adicioneUmMesNaDataProximaDesativacao(Vendedor vendedor) {
        Calendar novaData = Calendar.getInstance();
        Calendar dataAtual = null;
        Date proximaDesativacao = vendedor.getProximaDesativacao();

        if (proximaDesativacao != null) {
            dataAtual = Calendar.getInstance();
            dataAtual.setTime(proximaDesativacao);
        }

        if (proximaDesativacao == null || novaData.after(dataAtual)) {
            novaData.add(Calendar.MONTH, 1);
        } else {
            novaData.setTime(proximaDesativacao);
            novaData.add(Calendar.MONTH, 1);
        }

        vendedor.setProximaDesativacao(novaData.getTime());
    }

    @Override
    public Response<List<UserDTO>> desativeUsuario(long userId, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Usuario usuario = userRepository.findById(userId);
            usuario.setActive(false);
            userRepository.save(usuario);
            return findAllUser(authenticated);
        } catch (PermissionDeniedException e) {
            return returnListUserInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<Category>> addCategory(String categoryName, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(categoryName);
            if (optionalCategory.isEmpty()) {
                categoryRepository.save(new Category(categoryName));
                return returnListCategoryInResponse(HttpStatus.OK, categoryRepository.findAll(), null);
            }
            return returnListCategoryInResponse(HttpStatus.BAD_REQUEST, categoryRepository.findAll(), Arrays.asList("O nome escolhido já existe!"));
        } catch (PermissionDeniedException e) {
            return returnListCategoryInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<Category>> deleteCategory(String categoryName, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(categoryName);
            if (optionalCategory.isPresent()) {
                categoryRepository.deleteById(optionalCategory.get().getId());
            }
            return returnListCategoryInResponse(HttpStatus.OK, categoryRepository.findAll(), null);
        } catch (PermissionDeniedException e) {
            return returnListCategoryInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<MoneyConfig>> findAllMoneyConfig(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListMoneyConfigInResponse(HttpStatus.OK, moneyConfigRepository.findAll(), null);
        } catch (PermissionDeniedException e) {
            return returnListMoneyConfigInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<MoneyConfig> editMoneyConfig(long id, double valorMínimo, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Optional<MoneyConfig> optional = moneyConfigRepository.findById(id);
            if (optional.isEmpty())
                return returnMoneyConfigInResponse(HttpStatus.NOT_FOUND, null, Arrays.asList(REGISTER_NOT_FOUND_MESSAGE));

            optional.get().setValorMínimoCompra(valorMínimo);

            return returnMoneyConfigInResponse(HttpStatus.OK, moneyConfigRepository.save(optional.get()), null);

        } catch (PermissionDeniedException e) {
            return returnMoneyConfigInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<UserDTO>> findAllUser(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListUserInResponse(HttpStatus.OK, userMapper.toUserDTOList(userRepository.findAll()), null);

        } catch (PermissionDeniedException e) {
            return returnListUserInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<ProductDTO>> findAllProduct(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListProductInResponse(HttpStatus.OK, productMapper.toProductDTOS(productRepository.findAll()), null);

        } catch (PermissionDeniedException e) {
            return returnListProductInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<VendaDTO>> findAllVenda(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListVendaInResponse(HttpStatus.OK, vendaMapper.toVendaDTOs(vendaRepository.findAll()), null);

        } catch (PermissionDeniedException e) {
            return returnListVendaInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<CompraDTO>> findAllCompra(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListCompraInResponse(HttpStatus.OK, compraMapper.toCompraDTOS(compraRepository.findAll()), null);

        } catch (PermissionDeniedException e) {
            return returnListCompraInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<EntregaDTO>> findAllEntrega(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListEntregaInResponse(HttpStatus.OK, entregaMapper.toEntregaDTOs(entregaRepository.findAll()), null);

        } catch (PermissionDeniedException e) {
            return returnListEntregaInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<Taxa> editTaxa(long id, double percentual, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Optional<Taxa> optional = taxaRepository.findById(id);
            if (optional.isEmpty())
                return returnTaxaInResponse(HttpStatus.NOT_FOUND, null, Arrays.asList(REGISTER_NOT_FOUND_MESSAGE));

            optional.get().setPercentual(percentual);

            return returnTaxaInResponse(HttpStatus.OK, taxaRepository.save(optional.get()), null);

        } catch (PermissionDeniedException e) {
            return returnTaxaInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<RemuneracaoEntregadoresConfig>> findAllMoneyConfigDelivery(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return returnListMoneyConfigDeliveryInResponse(HttpStatus.OK, remuneracaoEntregadoresConfigRepository.findAll(), null);

        } catch (PermissionDeniedException e) {
            return returnListMoneyConfigDeliveryInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<RemuneracaoEntregadoresConfig> editMoneyConfigDelivery(RemuneracaoEntregadoresConfig config, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Optional<RemuneracaoEntregadoresConfig> optional = remuneracaoEntregadoresConfigRepository.findById(config.getId());
            if (optional.isEmpty())
                return returnMoneyConfigDeliveryInResponse(HttpStatus.NOT_FOUND, null, Arrays.asList(REGISTER_NOT_FOUND_MESSAGE));

            return returnMoneyConfigDeliveryInResponse(HttpStatus.OK, remuneracaoEntregadoresConfigRepository.save(config), null);

        } catch (PermissionDeniedException e) {
            return returnMoneyConfigDeliveryInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    /**
     * REALIZANDO PAGAMENTO PARA VENDEDOR E/OU MOTORISTA
     *  * Verificar permissão do usuário logado
     *  * Verificar o saldo. Se o saldo for insuficiente já retornar erro
     *  * Solicitar autorização (requisição PagSeguro)
     *  * Autorizar
     */
    public Response<HttpStatus> pay(UserAuthenticated authenticated, long userId) {
        try {
            verifiquePermissao(authenticated);
            Optional<Conta> optionalConta = contaRepository.findByUsuarioId(userId);
            if (optionalConta.isEmpty()) {
                return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, Arrays.asList(CONTA_NAO_PREENCHIDA_MESSAGE));
            }
            BigDecimal valor = optionalConta.get().getSaldo();
            String email = optionalConta.get().getEmailPagSeguro();
            if (email == null || email.isBlank() || email.isEmpty()) {
                return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND, Arrays.asList(CONTA_SEM_EMAIL_PAGSEGURO));
            }
            if (!saldoSuficiente(valor)) {
                return returnHttpStatusResponse(HttpStatus.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE, Arrays.asList(SALDO_INSUFICIENTE_MESSAGE));
            }
            return efetuaPagamento(optionalConta.get());

        } catch (PermissionDeniedException e) {
            return returnHttpStatusResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    private Response<HttpStatus> efetuaPagamento(Conta conta) {
        String transferCode = pagSeguroService.solicitaAutorizacao(conta.getEmailPagSeguro(), conta.getSaldo());
        String authorizationCode = "";
        boolean podeAutorizar = false;

        while (!podeAutorizar) {
            pauseThread(2000);
            Optional<TransferInfos> optionalTI = transferInfosRepository.findTransferInfosByTransferCode(transferCode);
            if (optionalTI.isPresent()) {
                authorizationCode = optionalTI.get().getAuthorizationCode();
                podeAutorizar = true;

                //REGISTAR HISTORICO DE PAGAMENTO
                historicoPagamentoService.saveOrUpdate(conta,podeAutorizar,transferCode);

            }
        }
        PagSeguroServiceImpl.JsonResponse jsonResponse = pagSeguroService.autorizaPagamento(authorizationCode);
        if (jsonResponse.isError())
            return returnHttpStatusResponse(HttpStatus.NOT_ACCEPTABLE, HttpStatus.NOT_ACCEPTABLE, Arrays.asList("Não foi possível realizar transferência para o e-mail " + conta.getEmailPagSeguro()));

        atualizaSaldoUsuario(conta);
        return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.OK, null);
    }

    private void atualizaSaldoUsuario(Conta conta) {
        conta.setSaldo(new BigDecimal(0));
        contaRepository.save(conta);
    }

    private void pauseThread(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean saldoSuficiente(BigDecimal valor) {
        double saldo = pagSeguroService.consultaSaldo();
        return valor.compareTo(new BigDecimal(saldo)) != 1;
    }

    @Override
    public Response<List<UserDTO>> createVendedor(UserAuthenticated authenticated, UserDTO vendedor) {
        try {
            verifiquePermissao(authenticated);
            verifyIfUserAlreadyExist(vendedor.getEmail());
            validaDocumentosVendedor(vendedor);
            ContaDTO conta = vendedor.getConta();
            vendedor.setConta(null);
            Vendedor vendedorMontado = montaVendedor(vendedor);
            contaService.internalCreateForNewVendedor(vendedorRepository.save(vendedorMontado), conta);
            return this.findAllUser(authenticated);
        } catch (RuntimeException e) {
            return returnListUserInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<ProductDTO>> deleteProduct(UserAuthenticated authenticated, long productId) {
        try {
            verifiquePermissao(authenticated);
            Product product = productRepository.findById(productId).get();
            Vendedor vendedor = (Vendedor) userRepository.findById(product.getVendedorId());
            vendedor.getProducts().remove(product);
            userRepository.save(vendedor);
            productRepository.deleteById(productId);
            return findAllProduct(authenticated);
        } catch (RuntimeException e) {
            return returnListProductInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public Response<List<Avaliacao>> findAllAvaliacao(UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData(avaliacaoRepository.findAll())
                    .withErrors(null)
                    .build();
        } catch (RuntimeException e) {
            return new Response.Builder()
                    .withStatus(HttpStatus.UNAUTHORIZED)
                    .withData(null)
                    .withErrors(Arrays.asList(e.getMessage()))
                    .build();
        }
    }

    @Override
    public Response<List<CompraDTO>> alterStatusCompra(long compraId, String newStatus, UserAuthenticated authenticated) {
        try {
            verifiquePermissao(authenticated);
            Compra compra = compraRepository.findById(compraId).get();
            compra.setStatus(StatusCompra.valueOf(newStatus.toUpperCase()));
            compraRepository.save(compra);
            return findAllCompra(authenticated);
        } catch (RuntimeException e) {
            return returnListCompraInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public void updateTransferInfosPagSeguro(TransferInfos transferInfos) {
        transferInfosRepository.save(transferInfos);
    }

    @Override
    public void canceleComprasNaoPagas(UserAuthenticated authenticated) {
        verifiquePermissao(authenticated);
        compraService.canceleComprasNaoPagas();
    }

    private Vendedor montaVendedor(UserDTO dto) {
        Vendedor vendedor = userMapper.vendedorFrom(dto);
        vendedor.setEndereco(enderecoService.saveEndereco(dto.getEndereco()).getData());
        vendedor.setPassword(Bcrypt.getHash(dto.getPassword()));
        vendedor.setAvatar(imageUrl);
        vendedor.setProximaDesativacao(getProximaDesativacao());

        return vendedor;
    }

    private Date getProximaDesativacao() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        return calendar.getTime();
    }

    private void validaDocumentosVendedor(UserDTO vendedor) throws CountryNotSupportedException {
        switch (vendedor.getCountry()) {
            case "BRA": //Brasil
                validadorDocumentos.validaVendedorBRA(vendedor.getCpf(), vendedor.getCnpj());
                break;
            case "AGO": //Angola
                validadorDocumentos.validaVendedorAGO(vendedor.getNif(), vendedor.getBilheteIdentidade());
                break;
            default:
                throw new CountryNotSupportedException("Não foi possível criar uma conta com o país " + vendedor.getCountry());
        }
    }

    private void verifyIfUserAlreadyExist(String email) {
        if (userRepository.findUserByEmailIgnoreCase(email).isPresent())
            throw new UserAlreadyExistException("This email already used");
    }

    private void verifiquePermissao(UserAuthenticated authenticated) {
        Usuario usuario = userRepository.findById(authenticated.getId());
        if (!usuario.getPerfil().equalsIgnoreCase("admin")) {
            throw new PermissionDeniedException("Você precisa ter permissão de administrador para poder realizar a ação solicitada!");
        }

    }

    private Response<List<RemuneracaoEntregadoresConfig>> returnListMoneyConfigDeliveryInResponse(HttpStatus status, List<RemuneracaoEntregadoresConfig> entregadoresConfigs, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(entregadoresConfigs)
                .withErrors(errors)
                .build();
    }

    private Response<RemuneracaoEntregadoresConfig> returnMoneyConfigDeliveryInResponse(HttpStatus status, RemuneracaoEntregadoresConfig config, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(config)
                .withErrors(errors)
                .build();
    }

    private Response<List<MoneyConfig>> returnListMoneyConfigInResponse(HttpStatus status, List<MoneyConfig> configList, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(configList)
                .withErrors(errors)
                .build();
    }

    private Response<List<ProductDTO>> returnListProductInResponse(HttpStatus status, List<ProductDTO> productDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(productDTOS)
                .withErrors(errors)
                .build();
    }

    private Response<List<EntregaDTO>> returnListEntregaInResponse(HttpStatus status, List<EntregaDTO> entregaDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(entregaDTOS)
                .withErrors(errors)
                .build();
    }

    private Response<List<CompraDTO>> returnListCompraInResponse(HttpStatus status, List<CompraDTO> compraDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(compraDTOS)
                .withErrors(errors)
                .build();
    }

    private Response<MoneyConfig> returnMoneyConfigInResponse(HttpStatus status, MoneyConfig config, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(config)
                .withErrors(errors)
                .build();
    }

    private Response<List<VendaDTO>> returnListVendaInResponse(HttpStatus status, List<VendaDTO> vendaDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(vendaDTOS)
                .withErrors(errors)
                .build();
    }

    private Response<List<UserDTO>> returnListUserInResponse(HttpStatus status, List<UserDTO> userDTOS, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(userDTOS)
                .withErrors(errors)
                .build();
    }

    private Response<List<Category>> returnListCategoryInResponse(HttpStatus status, List<Category> categories, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(categories)
                .withErrors(errors)
                .build();
    }

    private Response<HttpStatus> returnHttpStatusResponse(HttpStatus status, HttpStatus data, List<String> erros) {
        return new Response.Builder()
                .withStatus(status)
                .withData(data)
                .withErrors(erros)
                .build();
    }

    private Response<Taxa> returnTaxaInResponse(HttpStatus status, Taxa taxa, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(taxa)
                .withErrors(errors)
                .build();
    }

	@Override
	public Response aprovarDepoimento(long depoimentoId, UserAuthenticated authenticated) {
		 verifiquePermissao(authenticated);
		Depoimento depoimento = depoimentoRepository.findById(depoimentoId).get();
		if (depoimento != null) {
			depoimento.setStatus(StatusDepoimento.ACCEPTED);
			depoimentoRepository.save(depoimento);

			return returnHttpStatusResponse(HttpStatus.OK, null, null);
		}
		return returnHttpStatusResponse(HttpStatus.NOT_FOUND, null,
				Collections.singletonList("Não foi possivel aprovar o depoimento."));
	}
	
	@Override
	public SuperCategoryDTO create(SuperCategoryDTO superCategoryDTO,  UserAuthenticated authenticated) {
		verifiquePermissao(authenticated);
		SuperCategory superCategoria =  categoryMapper.toSuperCategoria(superCategoryDTO);
		SuperCategoryDTO dto = categoryMapper.fromSuperCategoria(superCategoryRepository.save(superCategoria));
		return dto;
	}

	@Override
	public CategoriaDTO create(CategoriaDTO categoryDTO, UserAuthenticated authenticated) {
		verifiquePermissao(authenticated);
		CategoryMapper categoryMapper = new CategoryMapper();
		Category category = categoryMapper.toEntity(categoryDTO);
		return categoryMapper.toDto(categoryRepository.save(category));
	}

	@Override
	public PromocaoFreteDTO save(PromocaoFreteDTO frete, UserAuthenticated authenticated) {
		verifiquePermissao(authenticated);
		PromocaoFreteDTO dto = promocaoFreteMapper.fromPromocaoFrete(promocaoFreteRepository.save(promocaoFreteMapper.toPromocaoFrete(frete)));
		return dto;
	}
}
