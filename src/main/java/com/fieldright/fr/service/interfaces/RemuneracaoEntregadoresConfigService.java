package com.fieldright.fr.service.interfaces;

import java.math.BigDecimal;
import java.util.Map;

public interface RemuneracaoEntregadoresConfigService {
    Map<String, BigDecimal> getValoresRemuneracao(Double distancia);
}
