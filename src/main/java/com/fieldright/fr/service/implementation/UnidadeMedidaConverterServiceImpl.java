package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Postagem;
import com.fieldright.fr.entity.UnidadeMedidaConverter;
import com.fieldright.fr.entity.dto.PostagemDTO;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.repository.UnidadeMedidaConverterRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.UnidadeMedidaConverterService;
import com.fieldright.fr.util.mapper.UnidadeMedidaConverterMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnidadeMedidaConverterServiceImpl implements UnidadeMedidaConverterService {

    @Autowired
    private UnidadeMedidaConverterRepository repository;

    @Autowired
    private UnidadeMedidaConverterMapper unidadeMedidaConverterMapper;


    @Override
    public Response<UnidadeMedidaConverterDTO> save(UnidadeMedidaConverterDTO dto) {


        UnidadeMedidaConverterDTO result = null;
        UnidadeMedidaConverter model = unidadeMedidaConverterMapper.toUnidadeMedidaConverter(dto);

        if(model != null){


            model = repository.save(model);
            if(model != null) {
                result = unidadeMedidaConverterMapper.toUnidadeMedidaConverterDTO(model);
            }
        }


        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(result)
                .withErrors(null)
                .build();

    }

    @Override
    public Response<UnidadeMedidaConverterDTO> update(Long id, UnidadeMedidaConverterDTO dto) {


        Optional<UnidadeMedidaConverter> optional =  repository.findById(id);

        if(optional.isPresent())
        {
            UnidadeMedidaConverter modelo = optional.get();
            unidadeMedidaConverterMapper.toUnidadeMedidaConverter(dto,modelo);

            modelo = repository.save(modelo);


            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData(modelo)
                    .withErrors(null)
                    .build();
        }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData("ID nao invalido")
                .withErrors(null)
                .build();

    }

    @Override
    public Response delete(Long id) {

        Optional<UnidadeMedidaConverter> optional =  repository.findById(id);

        if(optional.isPresent())
        {
            UnidadeMedidaConverter modelo = optional.get();

            repository.delete(modelo);


            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData("Unidade de Equivalencia  eliminada")
                    .withErrors(null)
                    .build();
        }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData("ID nao invalido")
                .withErrors(null)
                .build();


    }

    @Override
    public Response<Page<UnidadeMedidaConverterDTO>> findAll(Pageable pageable) {


        Page<UnidadeMedidaConverter> unidadeMedidaConverterPage = null;
        String mensagem = "Status invalido";

        try {

            unidadeMedidaConverterPage = repository.findAll(pageable);

            Page<UnidadeMedidaConverterDTO> dtos = unidadeMedidaConverterPage.map(p -> {
                return unidadeMedidaConverterMapper.toUnidadeMedidaConverterDTO(p);

            });


            return new Response.Builder()
                    .withStatus(HttpStatus.OK)
                    .withData(dtos)
                    .withErrors(null)
                    .build();


        }catch (Exception ex)
        {
            ex.printStackTrace();
            mensagem = ex.getMessage();
        }


        return new Response.Builder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withData(mensagem)
                .withErrors(null)
                .build();

    }



    @Override
    public UnidadeMedidaConverter findByUnidadeSimbolo(String unidadeOrigem,String unidadeDestino) {

        Optional<UnidadeMedidaConverter> model = repository.findByUnidadeOrigem(unidadeOrigem,unidadeDestino);

        if(model.isPresent())
        {
            return model.get();
        }
        return null;
    }

}
