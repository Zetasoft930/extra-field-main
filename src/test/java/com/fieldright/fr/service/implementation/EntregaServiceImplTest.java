package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.entity.Entrega;
import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.dto.EntregaDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.repository.CidadeAtuacaoRepository;
import com.fieldright.fr.repository.EntregaRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.EntregaService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.enums.StatusEntrega;
import com.fieldright.fr.util.enums.TipoVeiculo;
import com.fieldright.fr.util.mapper.EntregaMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EntregaServiceImplTest {

    private EntregaService service;
    @Mock
    private UserService userService;
    @Mock
    private EntregaRepository entregaRepository;
    @Mock
    private CidadeAtuacaoRepository cidadeAtuacaoRepository;

    @Before
    public void setUp() {
        service = new EntregaServiceImpl(entregaRepository, cidadeAtuacaoRepository, userService, new EntregaMapper());
    }

    @Test
    public void naoPodeAceitarUmaEntregaNaoNova() throws PSQLException {
        Entrega entrega = new Entrega();
        entrega.setStatusEntrega(StatusEntrega.TOMADA);

        when(userService.internalFindUserById(23L)).thenReturn(new Motorista());
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));

        Response<EntregaDTO> response = service.aceite(1L, getUserAuthenticated());

        verify(entregaRepository, times(0)).save(any(Entrega.class));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatus());
    }

    @Test
    public void deveAceitarEntrega() {
        UserAuthenticated authenticated = getUserAuthenticated();
        Map<String, BigDecimal> valoresRemuneracao = Map.of(TipoVeiculo.CARRO.toString(), new BigDecimal(50));
        Entrega entrega = getEntrega(valoresRemuneracao, null, null, 1L, "comprador1", StatusEntrega.NOVA);

        Motorista motorista = new Motorista();
        motorista.setFirstName("Pacifique");
        motorista.setLastName("Mukuna");
        motorista.setTipoVeiculo(TipoVeiculo.CARRO);

        when(userService.internalFindUserById(23L)).thenReturn(motorista);
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(entregaRepository.save(any(Entrega.class))).thenReturn(entrega);
        doNothing().when(entregaRepository).atualizeEntregasRejeitadas(anyLong());

        Response<EntregaDTO> response = service.aceite(1L, authenticated);

        verify(entregaRepository, times(1)).save(any(Entrega.class));
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void naoPodeAceitarUmaEntregaRejeitada() {
        UserAuthenticated authenticated = getUserAuthenticated();
        Map<String, BigDecimal> valoresRemuneracao = Map.of(TipoVeiculo.CARRO.toString(), new BigDecimal(50));
        Entrega entrega = getEntrega(valoresRemuneracao, null, null, 1L, "comprador1", StatusEntrega.NOVA);

        Motorista motorista = new Motorista();
        motorista.setFirstName("Pacifique");
        motorista.setLastName("Mukuna");
        motorista.setTipoVeiculo(TipoVeiculo.CARRO);
        motorista.getEntregasRejeitadas().add(entrega);

        when(userService.internalFindUserById(23L)).thenReturn(motorista);
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));

        Response<EntregaDTO> response = service.aceite(1L, authenticated);

        verify(entregaRepository, times(0)).save(any(Entrega.class));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatus());
    }

    @Test
    public void deveAdicionarEntregaRejeitadaNaListaNegraDoMotorista() {
        UserAuthenticated authenticated = getUserAuthenticated();

        Motorista motorista = new Motorista();
        motorista.setId(authenticated.getId());
        motorista.setEmail(authenticated.getEmail());

        Entrega entrega = new Entrega();
        entrega.setStatusEntrega(StatusEntrega.NOVA);

        Optional<Entrega> entregaOp = Optional.of(entrega);

        when(userService.internalFindUserById(authenticated.getId())).thenReturn(motorista);
        when(entregaRepository.findById(anyLong())).thenReturn(entregaOp);

        service.rejeita(1L, authenticated);

        assertEquals(1, motorista.getEntregasRejeitadas().size());
        assertTrue(motorista.getEntregasRejeitadas().contains(entrega));
        verify(userService, times(1)).internalUpdateUser(motorista);
    }

    @Test
    public void deveRetornarErroQuandoEntregaInformadaNaoForNova() {
        UserAuthenticated authenticated = getUserAuthenticated();

        Motorista motorista = new Motorista();
        motorista.setId(authenticated.getId());
        motorista.setEmail(authenticated.getEmail());

        Entrega entrega = new Entrega();
        entrega.setStatusEntrega(StatusEntrega.TOMADA);

        Optional<Entrega> entregaOp = Optional.of(entrega);

        when(userService.internalFindUserById(authenticated.getId())).thenReturn(motorista);
        when(entregaRepository.findById(anyLong())).thenReturn(entregaOp);

        Response response = service.rejeita(1L, authenticated);

        assertEquals(0, motorista.getEntregasRejeitadas().size());
        verify(userService, times(0)).internalUpdateUser(motorista);
        assertEquals("Não foi possivel rejeitar a entrega informada.", response.getErrors().get(0));
    }

    @Test
    public void deveRetornarErroQuandoEntregaInformadaNaoExiste() {
        UserAuthenticated authenticated = getUserAuthenticated();

        Motorista motorista = new Motorista();
        motorista.setId(authenticated.getId());
        motorista.setEmail(authenticated.getEmail());

        Optional<Entrega> entregaOp = Optional.empty();

        when(userService.internalFindUserById(authenticated.getId())).thenReturn(motorista);
        when(entregaRepository.findById(anyLong())).thenReturn(entregaOp);

        Response response = service.rejeita(1L, authenticated);

        assertEquals(0, motorista.getEntregasRejeitadas().size());
        verify(userService, times(0)).internalUpdateUser(motorista);
        assertEquals("Não foi possivel rejeitar a entrega informada.", response.getErrors().get(0));
    }

    @Test
    public void naoDeveRetornarEntregasRejeitadasPeloEntregador() {
        UserAuthenticated authenticated = getUserAuthenticated();
        Map<String, BigDecimal> valoresRemuneracao = Map.of(TipoVeiculo.CARRO.toString(), new BigDecimal(50));
        Endereco enderecoEntrega = getEndereco("Florianópolis");
        Endereco enderecoLoja = getEndereco("Curitiba");
        Entrega entrega1 = getEntrega(valoresRemuneracao, enderecoEntrega, enderecoLoja, 1L, "comprador1", null);
        Entrega entrega2 = getEntrega(valoresRemuneracao, enderecoEntrega, enderecoLoja, 2L, "comprador2", null);

        Motorista motorista = new Motorista();
        motorista.setTipoVeiculo(TipoVeiculo.CARRO);
        motorista.setCidadeAtuacao(1L);
        motorista.getEntregasRejeitadas().add(entrega1);

        List<Entrega> entregas = new ArrayList<>();
        entregas.addAll(Arrays.asList(entrega1, entrega2));

        when(userService.internalFindUserById(23L)).thenReturn(motorista);
        when(entregaRepository.findAllByStatusEntregaIs(StatusEntrega.NOVA)).thenReturn(entregas);
        when(cidadeAtuacaoRepository.findCidadeAtuacaoById(any())).thenReturn(umaCidadeAtuacao());

        Response<List<EntregaDTO>> response = service.findAllDisponivel(authenticated);

        assertEquals(1, response.getData().size());
        assertEquals(2L, response.getData().get(0).getId());
    }

    @Test
    public void deveRetornarErroSeUsuarioNaoForEntregador() {
        UserAuthenticated authenticated = getUserAuthenticated();
        Comprador comprador = new Comprador();

        when(userService.internalFindUserById(23L)).thenReturn(comprador);

        Response<List<EntregaDTO>> response = service.findAllDisponivel(authenticated);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatus());
    }

    @Test
    public void deveRetornarNadaQuandoMotoristaNaoTiverCidadeAtuacaoConfigurada() {
        UserAuthenticated authenticated = getUserAuthenticated();
        Map<String, BigDecimal> valoresRemuneracao = Map.of(TipoVeiculo.CARRO.toString(), new BigDecimal(50));
        Endereco enderecoEntrega = getEndereco("Florianópolis");
        Endereco enderecoLoja = getEndereco("Curitiba");
        Entrega entrega1 = getEntrega(valoresRemuneracao, enderecoEntrega, enderecoLoja, 1L, "comprador1", null);
        Entrega entrega2 = getEntrega(valoresRemuneracao, enderecoEntrega, enderecoLoja, 2L, "comprador2", null);

        Motorista motorista = new Motorista();
        motorista.setTipoVeiculo(TipoVeiculo.CARRO);

        List<Entrega> entregas = new ArrayList<>();
        entregas.addAll(Arrays.asList(entrega1, entrega2));

        when(userService.internalFindUserById(23L)).thenReturn(motorista);
        when(entregaRepository.findAllByStatusEntregaIs(StatusEntrega.NOVA)).thenReturn(entregas);
        when(cidadeAtuacaoRepository.findCidadeAtuacaoById(any())).thenReturn(null);

        Response<List<EntregaDTO>> response = service.findAllDisponivel(authenticated);

        assertEquals(0, response.getData().size());
    }

    private CidadeAtuacao umaCidadeAtuacao() {
        CidadeAtuacao cidade = new CidadeAtuacao();
        cidade.setNomeMunicipio("Florianópolis");
        return cidade;
    }

    private Entrega getEntrega(Map<String, BigDecimal> valoresRemuneracao, Endereco enderecoEntrega, Endereco enderecoLoja, long id, String compradorName, StatusEntrega status) {
        Entrega entrega = new Entrega();
        entrega.setId(id);
        entrega.setStatusEntrega(status);
        entrega.setCompradorName(compradorName);
        entrega.setEnderecoEntrega(enderecoEntrega);
        entrega.setEnderecoLoja(enderecoLoja);
        entrega.setValoresRemuneracao(valoresRemuneracao);
        return entrega;
    }

    private UserAuthenticated getUserAuthenticated() {
        UserAuthenticated authenticated = new UserAuthenticated();
        authenticated.setId(23L);
        authenticated.setEmail("email@email.email");
        return authenticated;
    }

    private Endereco getEndereco(String cidade) {
        Endereco endereco = new Endereco();
        endereco.setCidade(cidade);
        return endereco;
    }
}
