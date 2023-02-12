package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.UnidadeMedidaConverter;
import com.fieldright.fr.entity.dto.UnidadeMedidaConverterDTO;
import com.fieldright.fr.repository.UnidadeMedidaConverterRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.service.interfaces.UnidadeMedidaConverterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnidadeMedidaConverterServiceImpl implements UnidadeMedidaConverterService {

    @Autowired
    private UnidadeMedidaConverterRepository repository;

   /* @Autowired
    private ModelMapper modelMapper;*/


    @Override
    public Response<UnidadeMedidaConverterDTO> save(UnidadeMedidaConverterDTO dto) {


        UnidadeMedidaConverterDTO result = null;
      //  UnidadeMedidaConverter model = modelMapper.map(dto,UnidadeMedidaConverter.class);

     //   if(model != null){


         //   model = repository.save(model);
        //    result = modelMapper.map(model,UnidadeMedidaConverterDTO.class);
      //  }


        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(result)
                .withErrors(null)
                .build();

    }

    @Override
    public Response<Page<UnidadeMedidaConverterDTO>> findAll() {
        return null;
    }

   /* @Override
    public Response<UnidadeMedidaConverterDTO> findByUnidadeOrigem(String unidade) {

        UnidadeMedidaConverterDTO result = null;
        Optional<UnidadeMedidaConverter> model = repository.findByUnidadeOrigem(unidade);

        if(model.isPresent()){



        }

        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(model.orElseThrow())
                .withErrors(null)
                .build();

    }*/

    /*@Override
    public UnidadeMedidaConverter findByUnidadeSimbolo(String unidade) {

        Optional<UnidadeMedidaConverter> model = repository.findByUnidadeOrigem(unidade);

        if(model.isPresent())
        {
            return model.get();
        }
        return null;
    }*/

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
