package com.fieldright.fr.util.validador;

import com.fieldright.fr.util.enums.TipoVeiculo;
import com.fieldright.fr.util.exception.CNHBlankException;
import com.fieldright.fr.util.exception.CPFAndCNPJBlankException;
import com.fieldright.fr.util.exception.CPFBlankException;
import com.fieldright.fr.util.exception.CartaConducaoBlankException;
import com.fieldright.fr.util.exception.NIFAndBilheteBlankException;
import com.fieldright.fr.util.exception.NIFBlankException;
import com.fieldright.fr.util.exception.RenavamBlankException;
import org.springframework.stereotype.Component;

/**
 * @author Pacifique Mukuna
 * @implNote Esta classe útil foi criada para realizar as validações de todos os documentos que o sistema pode suportar.
 * Foi criada pela necessidade de validar documentos diferentes quando for criar cadastro de vendedores ou motoristas
 * de países diferentes visto que os documentos obrigatórios pdoem ser diferentes.
 * Caso o sistema suportar mais um país, ou seja, mais algum tipo de documento, basta criar um método para validar
 * os documentos dos vendedores daquele país e um outro para os do motorista do mesmo país.
 * @since 07/2020
 */
@Component
public class ValidadorDocumentos {

    public void validaVendedorBRA(String cpf, String cnpj) {
        if (cpf == null && cnpj == null)
            throw new CPFAndCNPJBlankException("É obrigatório informar um CPF ou um CNPJ");
    }

    public void validaMotoristaBRA(String cpf, String renavam, String cnh, TipoVeiculo tipoVeiculo) {

        if (!tipoVeiculo.equals(TipoVeiculo.BIKE)) {
            if (renavam == null) {
                throw new RenavamBlankException("É obrigatório informar o RENAVAM do veículo");
            }
            if (cnh == null) {
                throw new CNHBlankException("É obrigatório informar o CNH do motorista");
            }
        }
        if (cpf == null) {
            throw new CPFBlankException("É obrigatório informar o CPF do motorista");
        }
    }

    public void validaVendedorAGO(String nif, String bilheteIdentidade) {
        if (nif == null && bilheteIdentidade == null)
            throw new NIFAndBilheteBlankException("É obrigatório informar o NIF ou o bilhete de identidade do vendedor");
    }

    public void validaMotoristaAGO(String bilheteIdentidade, String cartaConducao, TipoVeiculo tipoVeiculo) {

        if (!tipoVeiculo.equals(TipoVeiculo.BIKE)) {
            if (cartaConducao == null) {
                throw new CartaConducaoBlankException("É obrigatório informar a carta de condução do motorista");
            }
        }
        if (bilheteIdentidade == null) {
            throw new NIFBlankException("É obrigatório informar o bilhete de identidade do motorista");
        }
    }
}
