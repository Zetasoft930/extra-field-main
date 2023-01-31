package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.AvaliacaoProduct;
import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Compra;
import com.fieldright.fr.entity.Entrega;
import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.dto.AvaliacaoProductDTO;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.dto.EntregaDateDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.notification.PushSender;
import com.fieldright.fr.repository.CidadeAtuacaoRepository;
import com.fieldright.fr.repository.EntregaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.ContaService;
import com.fieldright.fr.service.interfaces.EntregaService;
import com.fieldright.fr.service.interfaces.ProductService;
import com.fieldright.fr.service.interfaces.RemuneracaoEntregadoresConfigService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.service.interfaces.VendaService;
import com.fieldright.fr.util.StringUtil;
import com.fieldright.fr.util.enums.StatusEntrega;
import com.fieldright.fr.util.enums.TipoVeiculo;
import com.fieldright.fr.util.mapper.EntregaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EntregaServiceImpl implements EntregaService {

    private static final String NAO_FOI_POSSIVEL_REALIZAR_A_ACAO_SOLICITADA_MESSAGE = "Não foi possível realizar a ação solicitada";
    private static final String PERMISSION_DENIED_PERFIL = "É preciso ser um entregador para realizar esta ação";
    @Autowired
    private PushSender pushSender;
    @Autowired
    private UserService userService;
    @Autowired
    private VendaService vendaService;
    @Autowired
    private ContaService contaService;
    @Autowired
    private EntregaMapper entregaMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private EntregaRepository entregaRepository;
    @Autowired
    private RemuneracaoEntregadoresConfigService remuneracaoEntregadoresConfigService;
    @Autowired
    private CidadeAtuacaoRepository cidadeAtuacaoRepository;

    protected EntregaServiceImpl(EntregaRepository entregaRepository, CidadeAtuacaoRepository cidadeAtuacaoRepository, UserService userService, EntregaMapper entregaMapper) {
        this.userService = userService;
        this.entregaMapper = entregaMapper;
        this.entregaRepository = entregaRepository;
        this.cidadeAtuacaoRepository = cidadeAtuacaoRepository;
    }

    @Override
    public void internalGereNovaEntrega(Venda venda) {
        String distanciaEntrega = venda.getCompras().get(0).getDistanciaEntrega();
        Entrega entrega = new Entrega.Builder()
                .withStatus(StatusEntrega.NOVA)
                .withVendaId(venda.getId())
                .withCompradorName(venda.getCompradorName())
                .withCompradorPhone(venda.getCompradorPhone())
                .withVendedorName(venda.getVendedorName())
                .withVendedorPhone(venda.getVendedorPhone())
                .withEnderecoLoja(venda.getCompras().get(0).getEnderecoLoja())
                .withEnderecoEntrega(venda.getCompras().get(0).getEnderecoEntrega())
                .withDistanciaEntrega(distanciaEntrega)
                .withPesoCubado(calculePesoCubadoParaEntrega(venda))
                .withValoresRemuneracao(getValoresRemuneracao(distanciaEntrega))
                .build();
        entregaRepository.save(entrega);
        avisaEntregaDisponivel(entrega);
    }

    private double calculePesoCubadoParaEntrega(Venda venda) {
        double pesoCubadoTotal = 0;
        for (Compra compra : venda.getCompras()) {
            Product product = productService.internalFindById(compra.getProductId());
            double pesoCubadoTotalProduto = product.getPesoCubado() * compra.getQtdComprada();
            pesoCubadoTotal = pesoCubadoTotal + pesoCubadoTotalProduto;
        }
        BigDecimal bigDecimal = new BigDecimal(pesoCubadoTotal).round(new MathContext(3, RoundingMode.HALF_UP));
        return bigDecimal.doubleValue();
    }

    private Map<String, BigDecimal> getValoresRemuneracao(String distanciaEntrega) {
        String distanciaStr = StringUtil.getTextBeforeElement(distanciaEntrega.toLowerCase(), " km");
        return remuneracaoEntregadoresConfigService.getValoresRemuneracao(Double.valueOf(distanciaStr));
    }

    /**
     * Avisar os entregadores.
     * Enviar notificação de acordo com a distância de entrega
     * * 0 - 4 km >> Bikes - Motos - Carros
     * * > 4km >> Motos - Carros
     * * países diferentes >> empresas
     *
     * @param entrega
     */
    private void avisaEntregaDisponivel(Entrega entrega) {
        String distancia = StringUtil.getTextBeforeElement(entrega.getDistanciaEntrega().toLowerCase(), " km");
        double distance = Double.valueOf(distancia);
        boolean sameCountry = entrega.getEnderecoLoja().getPais().equals(entrega.getEnderecoEntrega().getPais());

        if (sameCountry) {
            double pesoCubado = entrega.getPesoCubado();
            if (distance <= 4) {
                if (pesoCubado > 100 && pesoCubado <= 1200) {
                    pushSender.avisaEntregaDisponivel(
                            Arrays.asList(TipoVeiculo.CARRO),
                            new String[]{entrega.getEnderecoLoja().getCidade(), entrega.getEnderecoEntrega().getCidade()}
                    );
                } else if (pesoCubado > 20 && pesoCubado <= 100) {
                    pushSender.avisaEntregaDisponivel(
                            Arrays.asList(TipoVeiculo.MOTO, TipoVeiculo.CARRO),
                            new String[]{entrega.getEnderecoLoja().getCidade(), entrega.getEnderecoEntrega().getCidade()}
                    );
                } else {
                    pushSender.avisaEntregaDisponivel(
                            Arrays.asList(TipoVeiculo.BIKE, TipoVeiculo.MOTO, TipoVeiculo.CARRO),
                            new String[]{entrega.getEnderecoLoja().getCidade(), entrega.getEnderecoEntrega().getCidade()}
                    );
                }
            } else {
                if (pesoCubado > 100 && pesoCubado <= 1200) {
                    pushSender.avisaEntregaDisponivel(
                            Arrays.asList(TipoVeiculo.CARRO),
                            new String[]{entrega.getEnderecoLoja().getCidade(), entrega.getEnderecoEntrega().getCidade()}
                    );
                } else if (pesoCubado > 20 && pesoCubado <= 100) {
                    pushSender.avisaEntregaDisponivel(
                            Arrays.asList(TipoVeiculo.MOTO, TipoVeiculo.CARRO),
                            new String[]{entrega.getEnderecoLoja().getCidade(), entrega.getEnderecoEntrega().getCidade()}
                    );
                }
            }
        } else {
            pushSender.avisaEntregaDisponivel(Arrays.asList(TipoVeiculo.EMPRESA), null);
        }
    }

    /**
     * Só se pode aceitar uma entrega um usuário do tipo motorista
     * Só se pode aceitar uma entrega com status "NOVA"
     *
     * @param entregaId
     * @param authenticated
     * @return
     */
    @Override
    public Response<EntregaDTO> aceite(long entregaId, UserAuthenticated authenticated) {
        Motorista motorista = (Motorista) userService.internalFindUserById(authenticated.getId());
        Optional<Entrega> optional = entregaRepository.findById(entregaId);

        if (entregaInformadaEhNova(optional) && !entregaRejeitada(motorista, optional.get())) {
            Entrega entrega = optional.get();
            entrega.setValorRemuneracao(
                    entrega.getValoresRemuneracao().get(
                            motorista.getTipoVeiculo().toString()
                    )
            );
            entrega.setStatusEntrega(StatusEntrega.TOMADA);
            entrega.setEntregadorId(motorista.getId());
            entrega.setEntregadorName(motorista.getFullName());
            entrega.setEntregadorPhone(motorista.getPhone());
            entrega.setAceitaAt(new Timestamp(System.currentTimeMillis()));

            Entrega entregaSalva = entregaRepository.save(entrega);
            try {
                entregaRepository.atualizeEntregasRejeitadas(entregaId);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            return returnEntregaDTOInResponse(HttpStatus.OK, entregaMapper.toEntregaDTO(entregaSalva), null);
        }
        return returnEntregaDTOInResponse(HttpStatus.UNPROCESSABLE_ENTITY, null, Arrays.asList(NAO_FOI_POSSIVEL_REALIZAR_A_ACAO_SOLICITADA_MESSAGE));
    }

    /**
     * Só se pode alterar uma entrega com um usuário do tipo motorista
     * Só se pode alterar uma entrega com status "TOMADA"
     * O motorista logado deve ser aquele que aceitou a entrega.
     * Alterar o status da compra e da venda para "A CAMINHO"
     * Enviar notificação pro comprador que o a compra dele está à caminho
     *
     * @param entregaId
     * @param authenticated
     * @return
     */
    @Override
    public Response<EntregaDTO> aCaminho(long entregaId, UserAuthenticated authenticated) {
        Optional<Entrega> optional = entregaRepository.findById(entregaId);

        if (optional.isPresent()) {
            Entrega entrega = optional.get();
            Motorista motorista = (Motorista) userService.internalFindUserById(authenticated.getId());
            if (entrega.getStatusEntrega().equals(StatusEntrega.TOMADA) && entrega.getEntregadorId() == motorista.getId()) {
                entrega.setStatusEntrega(StatusEntrega.A_CAMINHO);
                entrega.setACaminhoAt(new Timestamp(System.currentTimeMillis()));
                vendaService.internalVendaACaminho(entrega.getVendaId());
                return returnEntregaDTOInResponse(HttpStatus.OK, entregaMapper.toEntregaDTO(entregaRepository.save(entrega)), null);
            }
        }
        return returnEntregaDTOInResponse(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList(NAO_FOI_POSSIVEL_REALIZAR_A_ACAO_SOLICITADA_MESSAGE));
    }

    /**
     * Só se pode finalizar uma entrega com status "A_CAMINHO"
     * O motorista logado deve ser aquele que aceitou a entrega.
     * Alterar o status da compra e da venda para "ENTREGUE/FINALIZADA"
     *
     * @param entregaId
     * @param authenticated
     * @return
     */
    @Override
    public Response<EntregaDTO> finalize(long entregaId, UserAuthenticated authenticated) {
        Optional<Entrega> optional = entregaRepository.findById(entregaId);

        if (optional.isPresent()) {
            Entrega entrega = optional.get();
            Motorista motorista = (Motorista) userService.internalFindUserById(authenticated.getId());
            if (entrega.getStatusEntrega().equals(StatusEntrega.A_CAMINHO) && entrega.getEntregadorId() == motorista.getId()) {
                entrega.setStatusEntrega(StatusEntrega.FINALIZADA);
                entrega.setEntregueAt(new Timestamp(System.currentTimeMillis()));
                vendaService.internalVendaFinalizada(entrega.getVendaId());
                contaService.internalAcrescentaSaldoMotorista(entrega);
                return returnEntregaDTOInResponse(HttpStatus.OK, entregaMapper.toEntregaDTO(entregaRepository.save(entrega)), null);
            }
        }
        return returnEntregaDTOInResponse(HttpStatus.NOT_ACCEPTABLE, null, Arrays.asList(NAO_FOI_POSSIVEL_REALIZAR_A_ACAO_SOLICITADA_MESSAGE));
    }

    @Override
    public Response<List<EntregaDTO>> historico(UserAuthenticated authenticated) {
        List<Entrega> allByEntregadorId = entregaRepository.findAllByEntregadorId(authenticated.getId());

        return returnListEntregaDTOInResponse(HttpStatus.OK, entregaMapper.toEntregaDTOs(allByEntregadorId), null);
    }

    @Override
    public Response<List<EntregaDTO>> findAllDisponivel(UserAuthenticated authenticated) {
        Usuario usuario = userService.internalFindUserById(authenticated.getId());
        if (usuario.getClass() == Motorista.class) {
            List<Entrega> entregaList = entregaRepository.findAllByStatusEntregaIs(StatusEntrega.NOVA);
            return retornaEntregasDisponiveisConformePerfilDoEntregador(entregaList, (Motorista) usuario);
        }
        return returnListEntregaDTOInResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(PERMISSION_DENIED_PERFIL));
    }

    @Override
    public Response rejeita(long entregaId, UserAuthenticated authenticated) {
        Motorista motorista = (Motorista) userService.internalFindUserById(authenticated.getId());
        Optional<Entrega> optional = entregaRepository.findById(entregaId);
        if (entregaInformadaEhNova(optional)) {
            motorista.getEntregasRejeitadas().add(optional.get());
            userService.internalUpdateUser(motorista);

            return retornaResponse(HttpStatus.OK, null, null);
        }
        return retornaResponse(HttpStatus.NOT_FOUND, null, Collections.singletonList("Não foi possivel rejeitar a entrega informada."));
    }

    private Response retornaResponse(HttpStatus status, Object data, List<String> errors) {
        return new Response.Builder()
                .withStatus(status)
                .withData(data)
                .withErrors(errors)
                .build();
    }

    private boolean entregaInformadaEhNova(Optional<Entrega> optional) {
        return optional.isPresent() && optional.get().getStatusEntrega().equals(StatusEntrega.NOVA);
    }

    private Response<List<EntregaDTO>> retornaEntregasDisponiveisConformePerfilDoEntregador(List<Entrega> entregas, Motorista motorista) {
        /**
         * Remover entregas que não são da cidade de atuação do entregador
         * Remover entregas que não são do tipo de veículo do entregador
         * Remover entregas da lista negra do entregador
         * Coloca o valor a ser recebido ao realizar a entrega
         */
        Iterator<Entrega> iterator = entregas.iterator();
        List<Entrega> entregasToRemove = new ArrayList<>();
        while (iterator.hasNext()) {
            Entrega entrega = iterator.next();
            if (deveRemoverEntrega(motorista, entrega)) {
                entregasToRemove.add(entrega);
            } else {
                entrega.setValorRemuneracao(entrega.getValoresRemuneracao()
                        .get(motorista.getTipoVeiculo().toString()));
            }
        }
        entregas.removeAll(entregasToRemove);

        return returnListEntregaDTOInResponse(HttpStatus.OK, entregaMapper.toEntregaDTOs(entregas), null);
    }

    private boolean deveRemoverEntrega(Motorista motorista, Entrega entrega) {
        return !cidadeAtuacaoCompativel(motorista, entrega) || !tipoVeiculoCompativel(motorista, entrega) || entregaRejeitada(motorista, entrega);
    }

    private boolean entregaRejeitada(Motorista motorista, Entrega entrega) {
        return motorista.getEntregasRejeitadas().contains(entrega);
    }

    private boolean tipoVeiculoCompativel(Motorista motorista, Entrega entrega) {
        return entrega.getValoresRemuneracao().get(motorista.getTipoVeiculo().toString()) != null;
    }

    private boolean cidadeAtuacaoCompativel(Motorista motorista, Entrega entrega) {
        CidadeAtuacao cidadeAtuacao = cidadeAtuacaoRepository.findCidadeAtuacaoById(motorista.getCidadeAtuacao());
        if (cidadeAtuacao == null)
            return false;
        return cidadeAtuacao.getNomeMunicipio().equalsIgnoreCase(entrega.getEnderecoLoja().getCidade()) || cidadeAtuacao.getNomeMunicipio().equalsIgnoreCase(entrega.getEnderecoEntrega().getCidade());
    }

    private Response<List<EntregaDTO>> returnListEntregaDTOInResponse(HttpStatus status, List<EntregaDTO> entregaDTOS, List<String> errors) {
        return retornaResponse(status, entregaDTOS, errors);
    }

    private Response<EntregaDTO> returnEntregaDTOInResponse(HttpStatus status, EntregaDTO entregaDTO, List<String> errors) {
        return retornaResponse(status, entregaDTO, errors);
    }

	@Override
	public Response<Page<EntregaDateDTO>> findByMotoristaAndDays(UserAuthenticated authenticated, int daysAgo,
			Pageable pageable) {
		Page<EntregaDateDTO> dtos = null;
		Page<Object[]> obj = entregaRepository.findByMotoristaAndDays(authenticated.getId(), daysAgo, pageable);

		dtos = obj.map(Object -> {
			return EntregaDateDTO.builder().date(Object[0].toString()).value(new BigDecimal(Object[1].toString()))
					.build();
		});
		return new Response.Builder().withStatus(HttpStatus.OK).withData(dtos).withErrors(null).build();
	}

	@Override
	public Response<Page<EntregaDateDTO>> getLastEntregas(UserAuthenticated authenticated, Pageable pageable) {
		Page<EntregaDateDTO> dtos = null;
		List<EntregaDateDTO> list = new ArrayList<>();
		EntregaDateDTO dto = new EntregaDateDTO();
		Page<Object[]> objs = entregaRepository.getLastEntregasByUser(authenticated.getId(), pageable);
		for (Object[] obj : objs) {
			Product product = productService.internalFindById(Long.valueOf(obj[0].toString()));
			AvaliacaoProductDTO avaliacaoProduct = productService
					.findAvaliacaoProductByProductAndAvaliador(product.getId(), authenticated.getId());
			if (avaliacaoProduct != null) {
				dto.setDepoimento(avaliacaoProduct.getComentario());
				dto.setEstrelas(avaliacaoProduct.getEstrelas());
			}
			dto.setLoja(product.getVendedorName());
			dto.setPictures(product.getPictures());
			dto.setProductName(product.getName());
			list.add(dto);
		}

		return new Response.Builder().withStatus(HttpStatus.OK).withData(list).withErrors(null).build();
	}
}
