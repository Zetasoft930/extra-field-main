package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.repository.EnderecoRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.EnderecoService;
import com.fieldright.fr.util.exception.EnderecoNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EnderecoServiceImpl implements EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    @Override
    public Response<Endereco> saveEndereco(Endereco endereco) {
        return new Response.Builder()
                .withStatus(HttpStatus.CREATED)
                .withData(repository.save(endereco))
                .withErrors(null)
                .build();
    }

    @Override
    public void updateEndereco(Endereco newEndereco, long oldEnderecoId) {
        Optional<Endereco> byId = repository.findById(oldEnderecoId);
        if (!byId.isPresent())
            throw new EnderecoNotFoundException("Não foi encontrado este endereço");

        newEndereco.setId(oldEnderecoId);
        repository.save(newEndereco);
    }
}
