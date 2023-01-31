package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Conta;
import com.fieldright.fr.entity.Venda;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.repository.ContaRepository;
import com.fieldright.fr.repository.TaxaRepository;
import com.fieldright.fr.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContaServiceImplTest {

    private ContaServiceImpl service;
    @Mock
    private UserService userService;
    @Mock
    private ContaRepository contaRepository;
    @Mock
    private TaxaRepository taxaRepository;

    @Before
    public void setUp() {
        service = new ContaServiceImpl(userService, contaRepository, taxaRepository);
    }

    @Test
    public void deveConsiderarTaxaDaFieldrightETaxaDeEntregaAoAcrescentarSaldoDoVendedor() {
        Venda venda = new Venda();
        venda.setVlTotal(new BigDecimal(250));

        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal(0));
        Optional<Conta> opcionalConta = Optional.of(conta);

        Taxa taxaFieldright = new Taxa();
        Taxa taxaEntrega = new Taxa();
        taxaFieldright.setPercentual(10);
        taxaEntrega.setPercentual(3);

        Optional<Taxa> optionalTaxaFieldright = Optional.of(taxaFieldright);
        Optional<Taxa> optionalTaxaEntrega = Optional.of(taxaEntrega);

        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(false);

        when(contaRepository.findByUsuarioId(anyLong())).thenReturn(opcionalConta);
        when(taxaRepository.findById(1L)).thenReturn(optionalTaxaFieldright);
        when(taxaRepository.findById(2L)).thenReturn(optionalTaxaEntrega);
        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);

        service.internalAcrescentaSaldoVendedor(venda);

        Assertions.assertEquals(new BigDecimal(217.5), conta.getSaldo());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void deveConsiderarApenasTaxaDaFieldrightAoAcrescentarSaldoDoVendedorQuePossuiEntregadoresProprio() {
        Venda venda = new Venda();
        venda.setVlTotal(new BigDecimal(250));

        Conta conta = new Conta();
        conta.setSaldo(new BigDecimal(0));
        Optional<Conta> opcionalConta = Optional.of(conta);

        Taxa taxaFieldright = new Taxa();
        Taxa taxaEntrega = new Taxa();
        taxaFieldright.setPercentual(10);
        taxaEntrega.setPercentual(3);

        Optional<Taxa> optionalTaxaFieldright = Optional.of(taxaFieldright);
        Optional<Taxa> optionalTaxaEntrega = Optional.of(taxaEntrega);

        Vendedor vendedor = new Vendedor();
        vendedor.setPossuiEntregadores(true);

        when(contaRepository.findByUsuarioId(anyLong())).thenReturn(opcionalConta);
        when(taxaRepository.findById(1L)).thenReturn(optionalTaxaFieldright);
        when(taxaRepository.findById(2L)).thenReturn(optionalTaxaEntrega);
        when(userService.internalFindUserById(anyLong())).thenReturn(vendedor);

        service.internalAcrescentaSaldoVendedor(venda);

        Assertions.assertEquals(new BigDecimal(225.0), conta.getSaldo());
        verify(contaRepository, times(1)).save(conta);
    }
}
