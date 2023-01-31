package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import com.fieldright.fr.repository.RemuneracaoEntregadoresConfigRepository;
import com.fieldright.fr.service.interfaces.RemuneracaoEntregadoresConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemuneracaoEntregadoresConfigServiceImpl implements RemuneracaoEntregadoresConfigService {

    @Autowired
    private RemuneracaoEntregadoresConfigRepository repository;

    @Override
    public Map<String, BigDecimal> getValoresRemuneracao(Double distancia) {
        List<RemuneracaoEntregadoresConfig> configs = repository.findAll();
        Map<String, BigDecimal> valores = new HashMap<>(2);

        for (RemuneracaoEntregadoresConfig config : configs) {
            getValoresRemuneracao(distancia, config, valores);
        }

        return valores;
    }

    private void getValoresRemuneracao(Double distancia, RemuneracaoEntregadoresConfig config, Map<String, BigDecimal> valores) {
        double distanciaMaxima = config.getDistanciaMaxima();
        BigDecimal valorBase = config.getValorBase();
        if (distancia <= distanciaMaxima) {
            valores.put(config.getTipoVeiculo().name(), valorBase);

        } else if (distancia > distanciaMaxima && config.isUltraPassaDistanciaMaxima()) {
            double distanciaAdicional = distancia - distanciaMaxima;
            BigDecimal valorAdicional = config.getValorAdicionalPorKm().multiply(new BigDecimal(distanciaAdicional));
            valores.put(config.getTipoVeiculo().name(), valorBase.add(valorAdicional));
        }
    }
}
