package com.fieldright.fr.service.interfaces;

import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.response.Response;

public interface EnderecoService {

    Response<Endereco> saveEndereco(Endereco endereco);

    void updateEndereco(Endereco newEndereco, long oldEnderecoId);
}
